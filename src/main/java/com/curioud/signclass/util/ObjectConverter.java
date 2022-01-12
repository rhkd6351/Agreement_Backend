package com.curioud.signclass.util;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.submittee.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.*;
import com.curioud.signclass.dto.submittee.*;
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
                .studentName(vo.getStudentName())
                .studentId(vo.getStudentId())
                .activated(vo.getActivated())
                .regDate(vo.getRegDate())
                .submitteeObjectSigns(new ArrayList<>())
                .submitteeObjectTexts(new ArrayList<>())
                .submitteeObjectCheckboxes(new ArrayList<>())
                .build();
    }

    public SubmitteeObjectSignDTO submitteeObjectSignVOToDTO(SubmitteeObjectSignVO vo){
        return SubmitteeObjectSignDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
                .objectType(vo.getObjectType())
                .type(vo.getType())
                .build();
    }

    public SubmitteeObjectCheckboxDTO submitteeObjectCheckboxVOToDTO(SubmitteeObjectCheckboxVO vo){
        return SubmitteeObjectCheckboxDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
                .objectType(vo.getObjectType())
                .type(vo.getType())
                .checked(vo.isChecked())
                .color(vo.getColor())
                .build();
    }

    public SubmitteeObjectTextDTO submitteeObjectTextVOToDTO(SubmitteeObjectTextVO vo){
        return SubmitteeObjectTextDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .xPosition(vo.getXPosition())
                .yPosition(vo.getYPosition())
                .width(vo.getWidth())
                .height(vo.getHeight())
                .rotate(vo.getRotate())
                .page(vo.getPage())
                .objectType(vo.getObjectType())
                .type(vo.getType())
                .content(vo.getContent())
                .fontSize(vo.getFontSize())
                .color(vo.getColor())
                .build();
    }

    public SubmitteeObjectSignImgDTO submitteeObjectSignImgVOToDTO(SubmitteeObjectSignImgVO vo){
        return SubmitteeObjectSignImgDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .originalName(vo.getOriginalName())
                .saveName(vo.getSaveName())
                .size(vo.getSize())
                .uploadPath("*")
                .extension(vo.getExtension())
                .url(serverUrl + "/api/submittee/object/img/" + vo.getName())
                .regDate(vo.getRegDate())
                .build();
    }

}










