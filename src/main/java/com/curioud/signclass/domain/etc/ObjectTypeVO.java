package com.curioud.signclass.domain.etc;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OBJECT_TYPE_TB")
public class ObjectTypeVO {

    @Id
    @Column(name = "name", updatable = false, length = 45)
    private String name;

    @Column(name = "description", nullable = false, length = 45)
    private String description;
    
}
