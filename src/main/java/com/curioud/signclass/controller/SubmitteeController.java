package com.curioud.signclass.controller;

import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.service.project.ProjectService;
import com.curioud.signclass.service.submittee.SubmitteeObjectSignImgService;
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


    /** Get Project
     *
     * @param projectName 프로젝트 이름
     * @return 프로젝트 정보(오브젝트, pdf 포함)
     * @throws NotFoundException 유효하지 않은 pdf 혹은 object
     * @throws IOException pdf file 입출력 오류
     */
    @GetMapping("/project/{project-name}")
    public ResponseEntity<ProjectDTO> getProjectByNameWithoutAuthority(@PathVariable("project-name")String projectName) throws NotFoundException, IOException {

        ProjectDTO projectDTO = projectService.getWithProjectObjectsAndPdfByNameWithoutAuthority(projectName);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    /** Submit
     *
     * @param projectName 프로젝트 이름
     * @param mfList sign object와 연결된 이미지 리스트(optional)
     * @param pdf 작성 완료된 pdf파일
     * @param submitteeDTO 제출자의 학생이름 및 학번, 작성 완료된 object 리스트
     * @return 작성 완료된 pdf 파일
     * @throws NotFoundException 유효하지 않은 프로젝트 이름
     * @throws IOException 이미지 및 pdf 파일 입출력 오류
     * @throws NotSupportedException 지원하지 않는 이미지 및 pdf 확장자
     * @throws BadRequestException 이미지와 sign 오브젝트의 네이밍 규칙 위반
     */
    @PostMapping(path = "/project/{project-name}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] submitProject(
            @PathVariable("project-name")String projectName,
            @RequestPart(value = "sign_img", required = false) List<MultipartFile> mfList,
            @RequestPart(value = "file_pdf", required = true) MultipartFile pdf,
            @RequestPart(value = "data") SubmitteeDTO submitteeDTO) throws NotFoundException, IOException, NotSupportedException, BadRequestException {

        return submitteeService.saveWithObjectsAndPdf(projectName, submitteeDTO, mfList, pdf);
    }

    /**
     *
     * @param imgName 이미지 이름
     * @return 이미지 파일
     * @throws NotFoundException 유효하지 않은 이미지
     * @throws IOException 파일 입출력 오류
     */
    @GetMapping(path = "/object/img/{img-name}", produces = MediaType.IMAGE_JPEG_VALUE)
//    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] getObjectImage(@PathVariable(name = "img-name") String imgName) throws NotFoundException, IOException {
        return submitteeObjectSignImgService.getByteByName(imgName);
    }
}












