package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_OBJECT_SIGN_TB")
@DiscriminatorValue("OBJECT_TYPE_SIGN")
public class SubmitteeObjectSignVO extends SubmitteeObjectVO {

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "img_idx_fk", nullable = false)
    private SubmitteeObjectSignImgVO submitteeObjectSignImg;

    @Builder
    public SubmitteeObjectSignVO(String name, Disposition disposition, SubmitteeVO submittee, ObjectTypeVO objectType, String type, SubmitteeObjectSignImgVO submitteeObjectSignImg) {
        super(name, disposition, submittee, objectType);
        this.type = type;
        this.submitteeObjectSignImg = submitteeObjectSignImg;
    }

    public SubmitteeObjectSignDTO dto(){
        return SubmitteeObjectSignDTO.builder()
                .idx(getIdx())
                .name(getName())
                .xPosition(getDisposition().getXPosition())
                .yPosition(getDisposition().getYPosition())
                .width(getDisposition().getWidth())
                .height(getDisposition().getHeight())
                .rotate(getDisposition().getRotate())
                .page(getDisposition().getPage())
                .objectType(getObjectType().getName())
                .type(getType())
                .submitteeObjectSignImg(submitteeObjectSignImg.dto())
                .build();
    }
}