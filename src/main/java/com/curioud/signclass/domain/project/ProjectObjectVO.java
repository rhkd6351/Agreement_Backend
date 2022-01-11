package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_OBJECT_TB")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="object_type_fk")
public abstract class ProjectObjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "name")
    String name;

    @Column(name = "x_position")
    int xPosition;

    @Column(name = "y_position")
    int yPosition;

    @Column(name = "width")
    int width;

    @Column(name = "height")
    int height;

    @Column(name = "rotate")
    float rotate;

    @Column(name = "page")
    int page;

    @Column(name = "required")
    boolean required;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_idx_fk")
    ProjectVO project;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "object_type_fk", updatable = false, insertable = false) //TODO 이게 왜 동작하는지 알아낼것
    ObjectTypeVO objectType;
    
}
