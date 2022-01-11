package com.curioud.signclass.domain.submittee;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBMITTEE_OBJECT_TEXT_TB")
@DiscriminatorValue("OBJECT_TYPE_TEXT")
public class SubmitteeObjectTextVO extends SubmitteeObjectVO {

    @Column(name = "content")
    @Lob
    String content;

    @Column(name = "font_size")
    int fontSize;

    @Column(name = "color")
    String color;

    @Column(name = "type")
    String type;
    
}
