package com.curioud.signclass.util;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.user.UserDTO;

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
                .extension(vo.getExtension())
                .regDate(vo.getRegDate())
                .build();
    }

    public static ProjectDTO ProjectVOToDTO(ProjectVO vo){
        return ProjectDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .title(vo.getTitle())
                .description(vo.getDescription())
                .regDate(vo.getRegDate())
                .endDate(vo.getEndDate())
                .upDate(vo.getUpDate())
                .activated(vo.isActivated())
                .pdf(ObjectConverter.PdfVOToDTO(vo.getPdf()))
                .user(ObjectConverter.UserVOToDTO(vo.getUser()))
                .build();
    }

}










