package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.dto.submittee.SubmitteeObjectSignImgDTO;
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
@Table(name = "SUBMITTEE_OBJECT_SIGN_IMG_TB")
public class SubmitteeObjectSignImgVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "original_name", length = 255, nullable = false)
    private String originalName;

    @Column(name = "save_name", length = 255, nullable = false)
    private String saveName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "upload_path", length = 255, nullable = false)
    private String uploadPath;

    @Column(name = "extension", length = 45, nullable = false)
    private String extension;

    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;


    public SubmitteeObjectSignImgDTO dto() {
        SubmitteeObjectSignImgDTO submitteeObjectSignImgDTO = SubmitteeObjectSignImgDTO.builder()
                .idx(idx)
                .name(name)
                .originalName(originalName)
                .saveName(saveName)
                .size(size)
                .uploadPath("*")
                .extension(extension)
                .regDate(regDate)
                .build();

        submitteeObjectSignImgDTO.setUrl("/api/submittees/objects/img/" + name);
        return submitteeObjectSignImgDTO;
    }
}