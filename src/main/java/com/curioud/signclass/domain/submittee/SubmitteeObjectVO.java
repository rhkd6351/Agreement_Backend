package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBMITTEE_OBJECT_TB")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="object_type_fk")
public class SubmitteeObjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "name")
    String name;

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
    @JoinColumn(name = "submittee_idx_fk")
    SubmitteeVO submittee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_type_fk")
    ObjectTypeVO objectType;
    
}
