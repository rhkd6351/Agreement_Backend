package com.curioud.signclass.domain.project;


import com.curioud.signclass.dto.project.PdfDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PDF_TB")
public class PdfVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "name")
    private String name;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "save_name")
    private String saveName;

    @Column(name = "size")
    private Long size;

    @Column(name = "total_page")
    private int totalPage;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "extension")
    private String extension;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @OneToOne(mappedBy = "pdf")
    private ProjectVO project;

    @Builder
    public PdfVO(String name, String originalName, String saveName, Long size, int totalPage, String uploadPath, String extension) {
        this.name = name;
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.totalPage = totalPage;
        this.uploadPath = uploadPath;
        this.extension = extension;
    }

    public PdfDTO dto(){
        PdfDTO pdfDTO = PdfDTO.builder()
                .idx(idx)
                .name(name)
                .originalName(originalName)
                .saveName(saveName)
                .size(size)
                .totalPage(totalPage)
                .extension(extension)
                .regDate(regDate)
                .build();

        pdfDTO.setUrl(pdfDTO.getServerUrl() + "/api/projects/pdf/" + name);
        return pdfDTO;
    }

}
