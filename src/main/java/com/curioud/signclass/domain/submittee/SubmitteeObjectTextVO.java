package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.dto.submittee.SubmitteeObjectTextDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_OBJECT_TEXT_TB")
@DiscriminatorValue("OBJECT_TYPE_TEXT")
public class SubmitteeObjectTextVO extends SubmitteeObjectVO {

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "font_size", nullable = false)
    private int fontSize;

    @Column(name = "color", length = 20, nullable = false)
    private String color;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @Builder
    public SubmitteeObjectTextVO(String name, Disposition disposition, SubmitteeVO submittee, ObjectTypeVO objectType, String content, int fontSize, String color, String type) {
        super(name, disposition, submittee, objectType);
        this.content = content;
        this.fontSize = fontSize;
        this.color = color;
        this.type = type;
    }

    public SubmitteeObjectTextDTO dto(){
        return SubmitteeObjectTextDTO.builder()
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
                .content(content)
                .fontSize(fontSize)
                .color(color)
                .build();
    }

}






