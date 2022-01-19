package com.curioud.signclass.domain.submittee;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_PDF_TB")
public class SubmitteePdfVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name",length = 255, nullable = false)
    private String name;

    @Column(name = "original_name", length = 255, nullable = false)
    private String originalName;

    @Column(name = "save_name", length = 255, nullable = false)
    private String saveName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "total_page", nullable = false)
    private int totalPage;

    @Column(name = "upload_path", length = 255, nullable = false)
    private String uploadPath;

    @Column(name = "extension", length = 45, nullable = false)
    private String extension;

    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @OneToOne(mappedBy = "submitteePdf")
    private SubmitteeVO submitteeVO;

    @Builder
    public SubmitteePdfVO(String name, String originalName, String saveName, Long size, int totalPage, String uploadPath, String extension) {
        this.name = name;
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.totalPage = totalPage;
        this.uploadPath = uploadPath;
        this.extension = extension;
    }
}
