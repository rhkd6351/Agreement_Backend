package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.dto.project.ProjectObjectCheckboxDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROJECT_OBJECT_CHECKBOX_TB")
@DiscriminatorValue("OBJECT_TYPE_CHECKBOX")
@PrimaryKeyJoinColumn(name = "idx", referencedColumnName = "idx")
public class ProjectObjectCheckboxVO extends ProjectObjectVO {

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "type", length = 45)
    private String type;

    @Builder
    public ProjectObjectCheckboxVO(String name, Disposition disposition, boolean required, ObjectTypeVO objectType, ProjectVO project, String color, String type) {
        super(name, disposition, required, objectType, project);
        this.color = color;
        this.type = type;
    }

    public ProjectObjectCheckboxDTO dto(){
        return ProjectObjectCheckboxDTO.builder()
                .idx(getIdx())
                .name(getName())
                .xPosition(getDisposition().getXPosition())
                .yPosition(getDisposition().getYPosition())
                .width(getDisposition().getWidth())
                .height(getDisposition().getHeight())
                .rotate(getDisposition().getRotate())
                .page(getDisposition().getPage())
                .objectType(getObjectType().getName())
                .color(color)
                .type(type)
                .build();
    }

    public ProjectObjectCheckboxVO cloneObject(){

        return ProjectObjectCheckboxVO.builder()
                .name(getName())
                .disposition(getDisposition())
                .objectType(getObjectType())
                .color(color)
                .type(type)
                .build();

    }

}
