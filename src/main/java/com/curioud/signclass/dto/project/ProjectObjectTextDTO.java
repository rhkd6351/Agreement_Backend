package com.curioud.signclass.dto.project;


import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.project.ProjectObjectCheckboxVO;
import com.curioud.signclass.domain.project.ProjectObjectTextVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectTextDTO extends ProjectObjectDTO {

    @JsonProperty("font_size")
    int fontSize;

    String color;

    String type;

    public ProjectObjectTextVO toEntity() {
        return ProjectObjectTextVO.builder()
                .name(name)
                .disposition(new Disposition(xPosition, yPosition, width, height, rotate, page))
                .type(type)
                .color(color)
                .type(type)
                .fontSize(fontSize)
                .build();
    }
}
