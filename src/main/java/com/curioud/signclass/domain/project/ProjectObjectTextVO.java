package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.dto.project.ProjectObjectTextDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROJECT_OBJECT_TEXT_TB")
@DiscriminatorValue("OBJECT_TYPE_TEXT")
@PrimaryKeyJoinColumn(name = "idx", referencedColumnName = "idx")
public class ProjectObjectTextVO extends ProjectObjectVO {

    @Column(name = "font_size")
    private int fontSize;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "type", length = 45)
    private String type;

    @Builder
    public ProjectObjectTextVO(String name, Disposition disposition, boolean required, ObjectTypeVO objectType, ProjectVO project, int fontSize, String color, String type) {
        super(name, disposition, required, objectType, project);
        this.fontSize = fontSize;
        this.color = color;
        this.type = type;
    }

    public ProjectObjectTextDTO dto(){
        return ProjectObjectTextDTO.builder()
                .idx(getIdx())
                .name(getName())
                .xPosition(getDisposition().getXPosition())
                .yPosition(getDisposition().getYPosition())
                .width(getDisposition().getWidth())
                .height(getDisposition().getHeight())
                .rotate(getDisposition().getRotate())
                .page(getDisposition().getPage())
                .type(type)
                .color(color)
                .fontSize(fontSize)
                .objectType(getObjectType().getName())
                .build();
    }
}





