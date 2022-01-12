package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectSignDTO extends SubmitteeObjectDTO {

    String type;

    @JsonProperty("submittee_object_sign_img")
    SubmitteeObjectSignImgDTO submitteeObjectSignImg;

}