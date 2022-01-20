package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeObjectSignDTO extends SubmitteeObjectDTO {

    String type;

    @JsonProperty("submittee_object_sign_img")
    SubmitteeObjectSignImgDTO submitteeObjectSignImg;


    public SubmitteeObjectSignVO toEntity(SubmitteeObjectSignImgVO imgVO){
        return SubmitteeObjectSignVO.builder()
                .name(name)
                .disposition(new Disposition(xPosition, yPosition, width, height, rotate, page))
                .type(type)
                .submitteeObjectSignImg(imgVO)
                .build();
    }

    public Boolean isNameEqual(MultipartFile mf){
        int i = Objects.requireNonNull(mf.getOriginalFilename()).lastIndexOf(".");
        String fileName = mf.getOriginalFilename().substring(0, i);

        return this.name.equals(fileName);

    }

}