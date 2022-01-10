package com.curioud.signclass.dto.project;


import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectTextDTO extends ProjectObjectDTO {

    int fontSize;

    String color;

    String type;
}
