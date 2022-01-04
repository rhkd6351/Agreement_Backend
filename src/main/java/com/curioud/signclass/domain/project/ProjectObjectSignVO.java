package com.curioud.signclass.domain.project;


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
@Table(name = "PROJECT_OBJECT_SIGN_TB")
@DiscriminatorValue("OBJECT_TYPE_SIGN")
public class ProjectObjectSignVO extends ProjectObjectVO {

    @Column(name = "type")
    String type;
    
}
