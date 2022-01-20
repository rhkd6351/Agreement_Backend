package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.submittee.SubmitteeObjectCheckboxVO;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectCheckboxDTO extends SubmitteeObjectDTO {

    boolean checked;

    String color;

    String type;

    public SubmitteeObjectCheckboxVO toEntity(){
        return SubmitteeObjectCheckboxVO.builder()
                .name(name)
                .disposition(new Disposition(xPosition, yPosition, width, height, rotate, page))
                .type(type)
                .checked(checked)
                .color(color)
                .build();
    }

}
