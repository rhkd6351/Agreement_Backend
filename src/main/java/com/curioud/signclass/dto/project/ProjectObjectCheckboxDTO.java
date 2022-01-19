package com.curioud.signclass.dto.project;


import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.project.ProjectObjectCheckboxVO;
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

    public ProjectObjectCheckboxVO toEntity() {
        return ProjectObjectCheckboxVO.builder()
                .name(name)
                .disposition(new Disposition(xPosition, yPosition, width, height, rotate, page))
                .type(type)
                .color(color)
                .type(type)
                .build();
    }
}
