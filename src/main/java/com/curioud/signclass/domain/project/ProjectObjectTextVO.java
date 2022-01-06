package com.curioud.signclass.domain.project;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_OBJECT_TEXT_TB")
@DiscriminatorValue("OBJECT_TYPE_TEXT")
public class ProjectObjectTextVO extends ProjectObjectVO {

    @Column(name = "font_size")
    int fontSize;

    @Column(name = "color")
    String color;

    @Column(name = "type")
    String type;
    
}
