package com.curioud.signclass.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfDTO {

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

    @JsonProperty("original_width")
    float[] originalWidth;

    @Value("${server.url}")
    private String serverUrl;
}
