package com.curioud.signclass.dto.etc;

import lombok.Getter;

@Getter
public class MessageDTO {

    String message;

    public MessageDTO(String message) {
        this.message = message;
    }
}
