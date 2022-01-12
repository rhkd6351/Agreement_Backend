package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectSignImgDTO {

    Long idx;

    String name;

    @JsonProperty("original_name")
    String originalName;

    @JsonProperty("save_name")
    String saveName;

    Long size;

    @JsonProperty("upload_path")
    String uploadPath;

    String extension;

    String url;

    @JsonProperty("reg_date")
    LocalDateTime regDate;

}