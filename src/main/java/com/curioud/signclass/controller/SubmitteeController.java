package com.curioud.signclass.controller;

import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.service.project.ProjectService;
import com.curioud.signclass.service.submittee.SubmitteeObjectSignImgService;
import com.curioud.signclass.service.submittee.SubmitteePdfService;
import com.curioud.signclass.service.submittee.SubmitteeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/submittee")
public class SubmitteeController {


    @Autowired
    ProjectService projectService;

    @Autowired
    SubmitteeService submitteeService;

    @Autowired
    SubmitteeObjectSignImgService submitteeObjectSignImgService;


    /**
     *
     * @param projectName project name
     * @return project with objects, pdf
     * @throws NotFoundException 유효하지 않은 project name
     */
    @GetMapping("/project/{project-name}")
    public ResponseEntity<ProjectDTO> getProjectByNameWithoutAuthority(@PathVariable("project-name")String projectName) throws NotFoundException {

        ProjectDTO projectDTO = projectService.getWithProjectObjectsAndPdfByName(projectName);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    /**
     *
     * @param name 제출자 이름(난수)
     * @return
     * SubmitteeDTO 제출본 정보 (with pdf, objects)
     * @throws AuthException 열람 권한 없음 (본인이 등록한 프로젝트의 제출본이 아님)
     * @throws NotFoundException 유효하지 않은 submittee-name
     */
    @GetMapping("/{submittee-name}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<SubmitteeDTO> getSubmitteeWithPdfAndObjectsByName(@PathVariable("submittee-name") String name) throws AuthException, NotFoundException {

        SubmitteeDTO dto = submitteeService.getWithPdfAndObjectsByName(name);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     *
     * @param projectName 프로젝트 이름
     * @param mfList sign image 리스트
     * @param pdf 작성 완료된 pdf 저장
     * @param submitteeDTO
     * name
     * studentId
     * objects(sign, text, checkbox)
     *
     * @return 제출된 동의서의 pdf파일
     * @throws NotFoundException 유효하지 않은 project name
     * @throws IOException 이미지 저장 오류
     * @throws NotSupportedException 이미지 확장자 및 크기 예외처리
     * @throws BadRequestException 이미지 및 sign object 파일명 규칙 위반
     * (서로 같은 이름의 파일과 sign, 단 같은 파일끼리 또는 sign 끼리 이름이 동일하지 않아야 한다.)
     */
    @PostMapping(path = "/project/{project-name}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] submitProject(
            @PathVariable("project-name")String projectName,
            @RequestPart(value = "sign_img") List<MultipartFile> mfList,
            @RequestPart(value = "file_pdf") MultipartFile pdf,
            @RequestPart(value = "data") SubmitteeDTO submitteeDTO) throws NotFoundException, IOException, NotSupportedException, BadRequestException {

        return submitteeService.saveWithObjectsAndPdf(projectName, submitteeDTO, mfList, pdf);
    }

    @GetMapping(path = "/object/img/{img-name}", produces = MediaType.IMAGE_JPEG_VALUE)
//    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] getObjectImage(@PathVariable(name = "img-name") String imgName) throws NotFoundException, IOException {
        return submitteeObjectSignImgService.getByteByName(imgName);
    }
}












