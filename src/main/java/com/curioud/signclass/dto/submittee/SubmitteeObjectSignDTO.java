package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectSignDTO extends SubmitteeObjectDTO {

    String type;

    SubmitteeObjectSignImgVO submitteeObjectSignImg;

}