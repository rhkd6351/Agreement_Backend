package com.curioud.signclass.util;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.*;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ObjectConverter {

    String serverUrl;

    public ObjectConverter(@Value("${server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }


    public UserDTO userVOToDTO(UserVO vo) {
        return UserDTO.builder()
                .idx(vo.getIdx())
                .id(vo.getId())
                .password("*")
                .name(vo.getName())
                .regDate(vo.getRegDate())
                .auth(vo.getAuth().getName())
                .build();
    }

    public PdfDTO pdfVOToDTO(PdfVO vo) {
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

    public ProjectDTO projectVOToDTO(ProjectVO vo) {
        return ProjectDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .title(vo.getTitle())
                .description(vo.getDescription())
                .regDate(vo.getRegDate())
                .endDate(vo.getEndDate())
                .upDate(vo.getUpDate())
                .activated(vo.getActivated())
                .projectObjectCheckboxes(new ArrayList<>())
                .projectObjectSigns(new ArrayList<>())
                .projectObjectTexts(new ArrayList<>())
                .submittees(new ArrayList<>())
                .build();
    }

    public ProjectDTO projectVOToDTOWithSubmittees(ProjectVO vo) {
        ProjectDTO projectDTO = this.projectVOToDTO(vo);
        projectDTO.setSubmittees(vo.getSubmittees().stream().map(this::submitteeVOToDTO).collect(Collectors.toList()));
        return projectDTO;
    }

    public ProjectDTO projectVOToDTOWithUser(ProjectVO vo) {
        ProjectDTO projectDTO = this.projectVOToDTO(vo);
        projectDTO.setUser(this.userVOToDTO(vo.getUser()));
        return projectDTO;
    }

    public ProjectDTO projectVOToDTOWithUserAndPdf(ProjectVO vo) {
        ProjectDTO projectDTO = this.projectVOToDTO(vo);
        projectDTO.setPdf(this.pdfVOToDTO(vo.getPdf()));
        projectDTO.setUser(this.userVOToDTO(vo.getUser()));
        return projectDTO;
    }

//    public ProjectDTO projectVOToDTOWithObjects(ProjectVO vo) {
//        ProjectDTO projectDTO = this.projectVOToDTO(vo);
//        projectDTO.setProjectObjectSigns(vo.getProjectObjects().stream().map(this::projectObjectSignVOToDTO).collect(Collectors.toList()));
//        projectDTO.setProjectObjectTexts(vo.getProjectObjects().stream().map(this::projectObjectTextVOToDTO).collect(Collectors.toList()));
//        projectDTO.setProjectObjectCheckboxes(vo.getProjectObjects().stream().map(this::projectObjectCheckboxVOToDTO).collect(Collectors.toList()));
//
//        return projectDTO;
//    }



    public ProjectObjectDTO projectObjectVOToDTO(ProjectObjectVO vo) {
        return ProjectObjectDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
//                .objectType(vo.getObjectType().getName())
                .build();
    }

    public ProjectObjectSignDTO projectObjectSignVOToDTO(ProjectObjectSignVO vo) {
        return ProjectObjectSignDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
                .objectType(vo.getObjectType().getName())
                .type(vo.getType())
                .build();
    }

    public ProjectObjectTextDTO projectObjectTextVOToDTO(ProjectObjectTextVO vo) {
        return ProjectObjectTextDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
                .type(vo.getType())
                .color(vo.getColor())
                .fontSize(vo.getFontSize())
                .objectType(vo.getObjectType().getName())
                .build();
    }

    public ProjectObjectCheckboxDTO projectObjectCheckboxVOToDTO(ProjectObjectCheckboxVO vo) {
        return ProjectObjectCheckboxDTO.builder()
                .idx(vo.getIdx())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
                .objectType(vo.getObjectType().getName())
                .color(vo.getColor())
                .type(vo.getType())
                .build();
    }

    public SubmitteeDTO submitteeVOToDTO(SubmitteeVO vo) {

        return SubmitteeDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .regDate(vo.getRegDate())
                .build();
    }

}










