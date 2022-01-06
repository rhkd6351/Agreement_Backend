package com.curioud.signclass.dto.project;


import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectTextDTO extends ProjectObjectDTO {

    int fontSize;

    String color;

    String type;
}
