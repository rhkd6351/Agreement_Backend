package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.exception.BadRequestException;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROJECT_TB")
public class ProjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name", updatable = false, nullable = false, length = 100)
    private String name;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @UpdateTimestamp
    @Column(name = "up_date")
    private LocalDateTime upDate;

    //0: 생성됨, 1: 생성 후 1회 이상 작성됨, 2: 공유됨, 3: 공유 중단됨
    @Column(name = "activated")
    private int activated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx_fk")
    private UserVO user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_idx_fk")
    private PdfVO pdf;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<SubmitteeVO> submittees = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ProjectObjectVO> projectObjects = new HashSet<>();

    @Builder
    public ProjectVO(String name, String title, String description, int activated, UserVO user, PdfVO pdf) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.activated = activated;
        this.user = user;
        this.pdf = pdf;
    }

    public void updateTitle(String title) throws BadRequestException {
        if(title.length() >= 100)
            throw new BadRequestException("too long length of title");

        this.title = title;
    }

    public void updateState(int state) throws BadRequestException {

        if (this.activated == state)
            throw new BadRequestException("current state and changed state are the same");

        if(state == -1){
            this.activated = -1;
            return;
        }

        if (state < 1 || state > 3)
            throw new BadRequestException("invalid state : " + state);

        if (state == 3 && activated == 1)
            throw new BadRequestException("it is not a shared project.");


        this.activated = state;
    }

    public ProjectDTO dto(boolean pdf, boolean submittees, boolean objects) {
        List<ProjectObjectSignVO> projectObjectSigns = new ArrayList<>();
        List<ProjectObjectTextVO> projectObjectTexts = new ArrayList<>();
        List<ProjectObjectCheckboxVO> projectObjectCheckboxes = new ArrayList<>();

        if (objects) {
            for (ProjectObjectVO vo : projectObjects) {
                if (vo instanceof ProjectObjectSignVO)
                    projectObjectSigns.add((ProjectObjectSignVO) vo);
                else if (vo instanceof ProjectObjectCheckboxVO)
                    projectObjectCheckboxes.add((ProjectObjectCheckboxVO) vo);
                else if (vo instanceof ProjectObjectTextVO)
                    projectObjectTexts.add((ProjectObjectTextVO) vo);
            }
        }

        return ProjectDTO.builder()
                .idx(idx)
                .name(name)
                .title(title)
                .description(description)
                .regDate(regDate)
                .endDate(endDate)
                .upDate(upDate)
                .activated(activated)
                .projectObjectCheckboxes(projectObjectCheckboxes.stream().map(ProjectObjectCheckboxVO::dto).collect(Collectors.toList()))
                .projectObjectSigns(projectObjectSigns.stream().map(ProjectObjectSignVO::dto).collect(Collectors.toList()))
                .projectObjectTexts(projectObjectTexts.stream().map(ProjectObjectTextVO::dto).collect(Collectors.toList()))
                .submittees(submittees ? this.submittees.stream().map(i -> i.dto(false, null, false)).collect(Collectors.toList()) : new ArrayList<>())
                .submitteeCount(this.submittees.size())
                .pdf(pdf ? this.pdf.dto() : null)
                .build();
    }


    public void removeAllObjects() {
        this.getProjectObjects().clear();
    }

    public void addObject(ProjectObjectVO vo) {
        this.getProjectObjects().add(vo);
        vo.setProject(this);
    }

    public void addAllObjects(List<ProjectObjectVO> objects) {
        for (ProjectObjectVO vo : objects) {
            this.addObject(vo);
        }
    }

    //1: 생성됨, 2: 공유됨, 3: 공유 중단됨
    public boolean isEditable() throws IllegalAccessException {
        return activated != 2 && activated != 3;
    }

    public boolean ownershipCheck(UserVO user) {
        return this.user.getIdx() == user.getIdx();
    }

    public boolean isPublished() {
        return activated == 2;
    }



}
