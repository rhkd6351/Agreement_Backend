package com.curioud.signclass.dto.user;


import com.curioud.signclass.dto.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class UserDTO {

    Long idx;

    @NotBlank(groups = {ValidationGroups.userAuthenticationGroup.class}, message = "아이디가 입력되지 않았습니다.")
    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "아이디가 입력되지 않았습니다.")
    String id;

    @NotBlank(groups = {ValidationGroups.userAuthenticationGroup.class}, message = "비밀번호가 입력되지 않았습니다.")
    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "비밀번호가 입력되지 않았습니다.")
    String password;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "이름이 입력되지 않았습니다.")
    String name;

    String auth;

    @JsonProperty("reg_date")
    LocalDateTime regDate;

}
