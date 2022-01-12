package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBMITTEE_TB")
public class SubmitteeVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "name")
    String name;

    @Column(name = "student_name")
    String studentName;

    @Column(name = "student_id")
    int studentId;

    @Column(name = "activated")
    int activated;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx_fk")
    ProjectVO project;

    @OneToMany(mappedBy = "submittee")
    List<SubmitteeObjectVO> submitteeObjects;

    @OneToOne
    @JoinColumn(name = "submittee_pdf_idx_fk")
    SubmitteePdfVO submitteePdf;

//    @OneToMany(mappedBy = "submittee")
//    Set<SubmitteeObjectCheckboxVO> submitteeObjectCheckboxes;
//
//    @OneToMany(mappedBy = "submittee")
//    Set<SubmitteeObjectTextVO> submitteeObjectTexts;
//
//    @OneToMany(mappedBy = "submittee")
//    List<SubmitteeObjectSignVO> submitteeObjectSigns;
    
}
