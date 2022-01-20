package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.submittee.SubmitteeObjectTextVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectTextDTO extends SubmitteeObjectDTO {

    String content;

    @JsonProperty("font_size")
    int fontSize;

    String color;

    String type;

    public SubmitteeObjectTextVO toEntity(){
        return SubmitteeObjectTextVO.builder()
                .name(name)
                .disposition(new Disposition(xPosition, yPosition, width, height, rotate, page))
                .type(type)
                .content(content)
                .fontSize(fontSize)
                .color(color)
                .build();
    }
    
}
