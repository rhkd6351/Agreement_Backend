package com.curioud.signclass.domain.etc;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OBJECT_TYPE_TB")
public class ObjectTypeVO {

    @Id
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;
    
}
