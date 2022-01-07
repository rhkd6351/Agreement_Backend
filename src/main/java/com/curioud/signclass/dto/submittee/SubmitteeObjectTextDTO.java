package com.curioud.signclass.dto.submittee;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectTextDTO extends SubmitteeObjectDTO {

    String content;

    int fontSize;

    String color;

    String type;
    
}
