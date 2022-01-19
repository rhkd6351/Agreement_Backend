package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.dto.submittee.SubmitteeObjectCheckboxDTO;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_OBJECT_CHECKBOX_TB")
@DiscriminatorValue("OBJECT_TYPE_CHECKBOX")
public class SubmitteeObjectCheckboxVO extends SubmitteeObjectVO {

    @Column(name = "checked")
    private boolean checked;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private String type;

    @Builder
    public SubmitteeObjectCheckboxVO(String name, Disposition disposition, SubmitteeVO submittee, ObjectTypeVO objectType, boolean checked, String color, String type) {
        super(name, disposition, submittee, objectType);
        this.checked = checked;
        this.color = color;
        this.type = type;
    }

    public SubmitteeObjectCheckboxDTO dto(){
        return SubmitteeObjectCheckboxDTO.builder()
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
                .checked(checked)
                .color(color)
                .build();
    }

}





