package com.curioud.signclass.dto.submittee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteePdfDTO {

    Long idx;

    String name;

    @JsonProperty("original_name")
    String originalName;

    @JsonProperty("save_name")
    String saveName;

    Long size;

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("upload_path")
    String uploadPath;

    String extension;

    String url;

    @JsonProperty("reg_date")
    LocalDateTime regDate;
}
