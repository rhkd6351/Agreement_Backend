package com.curioud.signclass.dto.submittee;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${server.url}")
    private String serverUrl;
}