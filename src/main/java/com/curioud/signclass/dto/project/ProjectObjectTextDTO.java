package com.curioud.signclass.dto.project;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectTextDTO extends ProjectObjectDTO {

    @JsonProperty("font_size")
    int fontSize;

    String color;

    String type;
}
