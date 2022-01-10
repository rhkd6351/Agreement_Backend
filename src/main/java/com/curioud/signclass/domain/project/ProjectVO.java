package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.domain.user.UserVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //0: 생성됨 1: 공유됨 -1: 종료됨
    @Column(name="activated")
    int activated;

    @ManyToOne
    @JoinColumn(name = "user_idx_fk")
    UserVO user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_idx_fk")
    PdfVO pdf;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    Set<ProjectObjectVO> projectObjects = new HashSet<>();

//    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
//    Set<ProjectObjectCheckboxVO> projectObjectCheckboxes = new HashSet<>();
//
//    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
//    Set<ProjectObjectTextVO> projectObjectTexts = new HashSet<>();
//
//    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
//    Set<ProjectObjectSignVO> projectObjectSigns = new HashSet<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    List<SubmitteeVO> submittees = new ArrayList<>();
    
}
