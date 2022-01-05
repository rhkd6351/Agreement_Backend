package com.curioud.signclass.util;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.user.AuthVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.PdfDTO;
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

    public static PdfDTO PdfVOToDTO(PdfVO vo) {
        return PdfDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .originalName(vo.getOriginalName())
                .saveName(vo.getSaveName())
                .size(vo.getSize())
                .totalPage(vo.getTotalPage())
                .regDate(vo.getRegDate())
                .build();
    }

}
