package com.curioud.signclass.domain.submittee;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBMITTEE_OBJECT_SIGN_TB")
@DiscriminatorValue("OBJECT_TYPE_SIGN")
public class SubmitteeObjectSignVO extends SubmitteeObjectVO {

    @Column(name = "type")
    String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "img_idx_fk")
    SubmitteeObjectSignImgVO submitteeObjectSignImg;

}