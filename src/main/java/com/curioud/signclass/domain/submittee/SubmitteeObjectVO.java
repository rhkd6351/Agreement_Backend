package com.curioud.signclass.domain.submittee;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_OBJECT_TB")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="object_type_fk")
public class SubmitteeObjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Embedded
    private Disposition disposition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submittee_idx_fk", nullable = false)
    private SubmitteeVO submittee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_type_fk", updatable = false, insertable = false)
    private ObjectTypeVO objectType;


    public SubmitteeObjectVO(String name, Disposition disposition, SubmitteeVO submittee, ObjectTypeVO objectType) {
        this.name = name;
        this.disposition = disposition;
        this.submittee = submittee;
        this.objectType = objectType;
    }

    public void setSubmittee(SubmitteeVO submittee) {
        this.submittee = submittee;
    }
}
