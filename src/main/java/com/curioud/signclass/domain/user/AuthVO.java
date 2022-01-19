package com.curioud.signclass.domain.user;


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
@Table(name = "AUTH_TB")
public class AuthVO {

    @Id
    @Column(name = "name", updatable = false, length = 45)
    private String name;

    @Column(name = "description", length = 45, nullable = false)
    private String description;
    
}
