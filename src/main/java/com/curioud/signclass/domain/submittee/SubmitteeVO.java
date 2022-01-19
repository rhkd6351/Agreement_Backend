package com.curioud.signclass.domain.submittee;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_TB")
public class SubmitteeVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "student_name", length = 45, nullable = false)
    private String studentName;

    @Column(name = "student_id", nullable = false)
    private int studentId;

    @Column(name = "activated", nullable = false)
    private int activated;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx_fk", nullable = false)
    private ProjectVO project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submittee_pdf_idx_fk", nullable = false)
    private SubmitteePdfVO submitteePdf;

    @OneToMany(mappedBy = "submittee")
    private List<SubmitteeObjectVO> submitteeObjects;


    @Builder
    public SubmitteeVO(String name, String studentName, int studentId, int activated, ProjectVO project, SubmitteePdfVO submitteePdf) {
        this.name = name;
        this.studentName = studentName;
        this.studentId = studentId;
        this.activated = activated;
        this.project = project;
        this.submitteePdf = submitteePdf;
    }

    public SubmitteeDTO dto(){
        return SubmitteeDTO.builder()
                .idx(idx)
                .name(name)
                .studentName(studentName)
                .studentId(studentId)
                .activated(activated)
                .regDate(regDate)
                .submitteeObjectSigns(new ArrayList<>())
                .submitteeObjectTexts(new ArrayList<>())
                .submitteeObjectCheckboxes(new ArrayList<>())
                .build();
    }
}

//    @OneToMany(mappedBy = "submittee")
//    Set<SubmitteeObjectCheckboxVO> submitteeObjectCheckboxes;
//
//    @OneToMany(mappedBy = "submittee")
//    Set<SubmitteeObjectTextVO> submitteeObjectTexts;
//
//    @OneToMany(mappedBy = "submittee")
//    List<SubmitteeObjectSignVO> submitteeObjectSigns;
