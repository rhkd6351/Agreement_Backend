package com.curioud.signclass.dto.submittee;


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
    
}
