package com.curioud.signclass.dto.project;


import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.project.ProjectObjectSignVO;
import com.curioud.signclass.domain.project.ProjectObjectVO;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectSignDTO extends ProjectObjectDTO {

    String type;

    public ProjectObjectSignVO toEntity() {
        return ProjectObjectSignVO.builder()
                .name(name)
                .disposition(new Disposition(xPosition, yPosition, width, height, rotate, page))
                .type(type)
                .build();
    }

}
