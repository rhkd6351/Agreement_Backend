package com.curioud.signclass.util;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ObjectConverter {

    String serverUrl;

    public ObjectConverter(@Value("${server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }


    public UserDTO UserVOToDTO(UserVO vo) {
        return UserDTO.builder()
                .idx(vo.getIdx())
                .id(vo.getId())
                .password("*")
                .name(vo.getName())
                .regDate(vo.getRegDate())
                .auth(vo.getAuth().getName())
                .build();
    }

    public PdfDTO PdfVOToDTO(PdfVO vo) {
        return PdfDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .originalName(vo.getOriginalName())
                .saveName(vo.getSaveName())
                .size(vo.getSize())
                .totalPage(vo.getTotalPage())
                .extension(vo.getExtension())
                .regDate(vo.getRegDate())
                .url(serverUrl + "/api/project/pdf/" + vo.getName())
                .build();
    }

    public ProjectDTO ProjectVOToDTO(ProjectVO vo){
        return ProjectDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .title(vo.getTitle())
                .description(vo.getDescription())
                .regDate(vo.getRegDate())
                .endDate(vo.getEndDate())
                .upDate(vo.getUpDate())
                .activated(vo.isActivated())
                .pdf(this.PdfVOToDTO(vo.getPdf()))
                .user(this.UserVOToDTO(vo.getUser()))
                .build();
    }

}










