package com.curioud.signclass.dto.project;


import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectCheckboxDTO extends ProjectObjectDTO {

    String color;

    String type;
}
