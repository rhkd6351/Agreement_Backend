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
import java.util.Set;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_idx_fk")
    private PdfVO pdf;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<ProjectObjectVO> projectObjects = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<SubmitteeVO> submittees = new HashSet<>();

    @Builder
    public ProjectVO(String name, String title, String description, int activated, UserVO user, PdfVO pdf) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.activated = activated;
        this.user = user;
        this.pdf = pdf;
    }

    public void updateState(int state) throws BadRequestException {
        if (this.activated == state) {
            throw new BadRequestException("current state and changed state are the same");
        } else {
            this.activated = state;
        }
    }

    public ProjectDTO dto(boolean pdf){
        return ProjectDTO.builder()
                .idx(idx)
                .name(name)
                .title(title)
                .description(description)
                .regDate(regDate)
                .endDate(endDate)
                .upDate(upDate)
                .activated(activated)
                .projectObjectCheckboxes(new ArrayList<>())
                .projectObjectSigns(new ArrayList<>())
                .projectObjectTexts(new ArrayList<>())
                .submittees(new ArrayList<>())
                .pdf(pdf ? this.pdf.dto() : null)
                .build();
    }

}


//    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
//    Set<ProjectObjectCheckboxVO> projectObjectCheckboxes = new HashSet<>();
//
//    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
//    Set<ProjectObjectTextVO> projectObjectTexts = new HashSet<>();
//
//    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
//    Set<ProjectObjectSignVO> projectObjectSigns = new HashSet<>();
