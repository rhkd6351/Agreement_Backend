package com.curioud.signclass.dto.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class UserDTO {

    Long idx;

    String id;

    String password;

    String name;

    String auth;

    @JsonProperty("reg_date")
    LocalDateTime regDate;

}
