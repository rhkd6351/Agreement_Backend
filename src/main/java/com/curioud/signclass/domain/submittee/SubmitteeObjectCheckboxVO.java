package com.curioud.signclass.domain.submittee;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBMITTEE_OBJECT_CHECKBOX_TB")
@DiscriminatorValue("OBJECT_TYPE_CHECKBOX")
public class SubmitteeObjectCheckboxVO extends SubmitteeObjectVO {

    @Column(name = "checked")
    boolean checked;

    @Column(name = "color")
    String color;

    @Column(name = "type")
    String type;
    
}
