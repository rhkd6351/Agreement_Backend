package com.curioud.signclass.domain.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROJECT_OBJECT_TB")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="object_type_fk")
public abstract class ProjectObjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Embedded
    private Disposition disposition;

    @Column(name = "required", nullable = false)
    private boolean required;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_idx_fk")
    private ProjectVO project;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "object_type_fk", updatable = false, insertable = false)
    private ObjectTypeVO objectType;


    public ProjectObjectVO(String name, Disposition disposition, boolean required, ObjectTypeVO objectType, ProjectVO project) {
        this.name = name;
        this.disposition = disposition;
        this.required = required;
        this.objectType = objectType;
        this.project = project;
    }

    public void setProject(ProjectVO vo){
        this.project = vo;
    }



}
