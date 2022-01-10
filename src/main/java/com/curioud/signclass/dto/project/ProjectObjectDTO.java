package com.curioud.signclass.dto.project;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectDTO {

    Long idx;

    @JsonProperty("x_position")
    int xPosition;

    @JsonProperty("y_position")
    int yPosition;

    int width;

    int height;

    float rotate;

    int page;

    ProjectDTO project;

    @JsonProperty("object_type")
    String objectType;
    
}
