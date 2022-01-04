package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_OBJECT_TB")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="object_type_fk")
public class ProjectObjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "x_position")
    int x_position;

    @Column(name = "y_position")
    int y_position;

    @Column(name = "width")
    int width;

    @Column(name = "height")
    int height;

    @Column(name = "rotate")
    float rotate;

    @Column(name = "page")
    int page;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx_fk")
    ProjectVO project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_type_fk")
    ObjectTypeVO objectType;
    
}
