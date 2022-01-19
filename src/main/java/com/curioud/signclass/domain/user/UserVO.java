package com.curioud.signclass.domain.user;

import com.curioud.signclass.dto.user.UserDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "id", nullable = false, unique = true, length = 45, updatable = false)
    private String id;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @CreationTimestamp
    @Column(name = "reg_date", nullable = false, updatable = false)
    private LocalDateTime regDate;

    @Column(name="activated", nullable = false)
    private boolean activated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_fk", nullable = false)
    private AuthVO auth;

//    @OneToMany(mappedBy = "user", orphanRemoval = true)
//    private List<ProjectVO> projects;


    @Builder
    public UserVO(String id, String password, String name, boolean activated, AuthVO auth) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.activated = activated;
        this.auth = auth;
    }

    public UserDTO dto(){
        return UserDTO.builder()
                .idx(this.getIdx())
                .id(this.getId())
                .password("*")
                .name(this.getName())
                .regDate(this.getRegDate())
                .auth(this.getAuth().getName())
                .build();
    }

    public boolean isEqual(UserVO vo){
        return vo.getIdx() == this.idx;
    }

}
