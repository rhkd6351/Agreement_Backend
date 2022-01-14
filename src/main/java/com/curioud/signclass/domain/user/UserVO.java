package com.curioud.signclass.domain.user;


import com.curioud.signclass.domain.project.ProjectVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_TB")
public class UserVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "id")
    String id;

    @Column(name = "password")
    String password;

    @Column(name = "name")
    String name;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @Column(name="activated")
    boolean activated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_fk")
    AuthVO auth;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<ProjectVO> projects;
    
}
