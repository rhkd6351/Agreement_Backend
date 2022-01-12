package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBMITTEE_PDF_TB")
public class SubmitteePdfVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "name")
    String name;

    @Column(name = "original_name")
    String originalName;

    @Column(name = "save_name")
    String saveName;

    @Column(name = "size")
    Long size;

    @Column(name = "total_page")
    int totalPage;

    @Column(name = "upload_path")
    String uploadPath;

    @Column(name = "extension")
    String extension;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @OneToOne(mappedBy = "submitteePdf")
    SubmitteeVO submitteeVO;

}
