package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
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

    int x_position;

    int y_position;

    int width;

    int height;

    float rotate;

    int page;

    SubmitteeVO submittee;

    ObjectTypeVO objectType;
    
}
