package com.curioud.signclass.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfDTO {

    Long idx;

    String name;

    String originalName;

    String saveName;

    Long size;

    int totalPage;

    String uploadPath;

    LocalDateTime regDate;
}
