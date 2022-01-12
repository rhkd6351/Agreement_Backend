package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.etc.ObjectTypeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectDTO {

    Long idx;

    String name;

    @JsonProperty("x_position")
    int xPosition;

    @JsonProperty("y_position")
    int yPosition;

    int width;

    int height;

    float rotate;

    int page;

    SubmitteeDTO submittee;

    @JsonProperty("object_type")
    String objectType;
    
}
