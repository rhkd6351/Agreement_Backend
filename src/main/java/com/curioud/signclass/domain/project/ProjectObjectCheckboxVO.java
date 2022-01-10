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
@PrimaryKeyJoinColumn(name = "object_idx_fk")
public class ProjectObjectCheckboxVO extends ProjectObjectVO {

    @Column(name = "color")
    String color;

    @Column(name = "type")
    String type;
    
}
