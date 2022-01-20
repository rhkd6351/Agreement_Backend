package com.curioud.signclass.dto.user;

import lombok.Getter;

@Getter
public class TokenDTO {

    String jwt;

    public TokenDTO(String jwt) {
        this.jwt = jwt;
    }
}
