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
@Table(name = "PROJECT_OBJECT_CHECKBOX_TB")
@DiscriminatorValue("OBJECT_TYPE_CHECKBOX")
public class ProjectObjectCheckboxVO extends ProjectObjectVO {

    @Column(name = "color")
    String color;

    @Column(name = "type")
    String type;
    
}
