package com.curioud.signclass.util;

import com.curioud.signclass.domain.user.AuthVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.UserDTO;
import org.springframework.stereotype.Component;

//@Component
public class ObjectConverter {


    public static UserDTO UserVOToDTO(UserVO vo) {
        return UserDTO.builder()
                .idx(vo.getIdx())
                .id(vo.getId())
                .password("*")
                .name(vo.getName())
                .regDate(vo.getRegDate())
                .auth(vo.getAuth().getName())
                .build();
    }

}
