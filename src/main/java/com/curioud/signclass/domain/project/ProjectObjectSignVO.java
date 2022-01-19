package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.dto.project.ProjectObjectSignDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROJECT_OBJECT_SIGN_TB")
@DiscriminatorValue("OBJECT_TYPE_SIGN")
@PrimaryKeyJoinColumn(name = "idx", referencedColumnName = "idx")
public class ProjectObjectSignVO extends ProjectObjectVO {

    @Column(name = "type", length = 45)
    private String type;

    @Builder
    public ProjectObjectSignVO(String name, Disposition disposition, boolean required, ObjectTypeVO objectType, ProjectVO project, String type) {
        super(name, disposition, required, objectType, project);
        this.type = type;
    }

    public ProjectObjectSignDTO dto(){
        return ProjectObjectSignDTO.builder()
                .idx(getIdx())
                .name(getName())
                .xPosition(getDisposition().getXPosition())
                .yPosition(getDisposition().getYPosition())
                .width(getDisposition().getWidth())
                .height(getDisposition().getHeight())
                .rotate(getDisposition().getRotate())
                .page(getDisposition().getPage())
                .objectType(getObjectType().getName())
                .type(type)
                .build();
    }
}
