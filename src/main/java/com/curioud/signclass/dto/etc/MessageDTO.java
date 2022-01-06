package com.curioud.signclass.dto.etc;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MessageDTO {

    String message;

    public MessageDTO(String message) {
        this.message = message;
    }
}
