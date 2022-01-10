package com.curioud.signclass.domain.project;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_OBJECT_SIGN_TB")
@DiscriminatorValue("OBJECT_TYPE_SIGN")
@PrimaryKeyJoinColumn(name = "idx", referencedColumnName = "idx")
public class ProjectObjectSignVO extends ProjectObjectVO {

    @Column(name = "type")
    String type;

}
