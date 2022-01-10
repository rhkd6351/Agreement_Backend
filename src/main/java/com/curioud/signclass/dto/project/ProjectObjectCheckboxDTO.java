package com.curioud.signclass.dto.project;


import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectCheckboxDTO extends ProjectObjectDTO {

    String color;

    String type;
}
