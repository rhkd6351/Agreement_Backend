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
@Table(name = "PROJECT_OBJECT_TEXT_TB")
@DiscriminatorValue("OBJECT_TYPE_TEXT")
@PrimaryKeyJoinColumn(name = "object_idx_fk")
public class ProjectObjectTextVO extends ProjectObjectVO {

    @Column(name = "font_size")
    int fontSize;

    @Column(name = "color")
    String color;

    @Column(name = "type")
    String type;
    
}
