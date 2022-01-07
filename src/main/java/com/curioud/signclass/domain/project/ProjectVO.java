package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.domain.user.UserVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_TB")
public class ProjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "name")
    String name;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @Column(name = "end_date")
    LocalDateTime endDate;

    @Column(name = "up_date")
    @UpdateTimestamp
    LocalDateTime upDate;

    @Column(name="activated")
    boolean activated;

    @ManyToOne
    @JoinColumn(name = "user_idx_fk")
    UserVO user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_idx_fk")
    PdfVO pdf;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    List<ProjectObjectCheckboxVO> projectObjectCheckboxes;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    List<ProjectObjectTextVO> projectObjectTexts;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    List<ProjectObjectSignVO> projectObjectSigns;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    List<SubmitteeVO> submittees;
    
}
