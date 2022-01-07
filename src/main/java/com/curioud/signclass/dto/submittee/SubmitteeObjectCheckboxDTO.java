package com.curioud.signclass.dto.submittee;


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
    
}
