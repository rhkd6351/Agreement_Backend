package com.curioud.signclass.controller;

import com.curioud.signclass.dto.ValidationGroups;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.service.project.ProjectFindService;
import com.curioud.signclass.service.submittee.SubmitteeFindService;
import com.curioud.signclass.service.submittee.SubmitteeObjectSignImgService;
import com.curioud.signclass.service.submittee.SubmitteeUpdateService;
import javassist.NotFoundException;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/submittees")
public class SubmitteeController {


    ProjectFindService projectFindService;
    SubmitteeObjectSignImgService submitteeObjectSignImgService;
    SubmitteeUpdateService submitteeUpdateService;
    SubmitteeFindService submitteeFindService;

    public SubmitteeController(ProjectFindService projectFindService, SubmitteeObjectSignImgService submitteeObjectSignImgService, SubmitteeUpdateService submitteeUpdateService, SubmitteeFindService submitteeFindService) {
        this.projectFindService = projectFindService;
        this.submitteeObjectSignImgService = submitteeObjectSignImgService;
        this.submitteeUpdateService = submitteeUpdateService;
        this.submitteeFindService = submitteeFindService;
    }

    /** Get Project
     *
     * @param projectName 프로젝트 이름
     * @return 프로젝트 정보(오브젝트, pdf 포함)
     * @throws NotFoundException 유효하지 않은 pdf 혹은 object
     * @throws IOException pdf file 입출력 오류
     */
    @GetMapping("/projects/{project-name}")
    public ResponseEntity<ProjectDTO> getProjectByNameWithoutAuthority(@PathVariable("project-name")String projectName) throws NotFoundException, IOException {

        ProjectDTO projectDTO = projectFindService.getWithProjectObjectsAndPdfByNameWithoutAuth(projectName);

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
    @PostMapping(path = "/projects/{project-name}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> submitProject(
            @PathVariable("project-name")String projectName,
            @RequestPart(value = "sign_img", required = false) List<MultipartFile> mfList,
            @RequestPart(value = "file_pdf", required = true) MultipartFile pdf,
            @RequestPart(value = "data") @Validated(ValidationGroups.submitteeSubmitGroup.class) SubmitteeDTO submitteeDTO) throws NotFoundException, IOException, NotSupportedException, BadRequestException, AuthException {

        SubmitteeDTO submittee = submitteeUpdateService.saveWithObjectsAndPdf(projectName, submitteeDTO, mfList, pdf);
        ProjectDTO project = submittee.getProject();

        String fileName = project.getTitle().replace(" ", "") +  "_" + submittee.getStudentId() + "_" + submittee.getStudentName() + "_" + submittee.getRegDate().format(DateTimeFormatter.ofPattern("yyMMdd_HHmmss"));
        String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        //헤더 파일명 입력
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(encodedName).build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(submitteeFindService.getSubmitteePdfByName(submittee.getName(), false), headers, HttpStatus.OK);
    }

    /**
     *
     * @param imgName 이미지 이름
     * @return 이미지 파일
     * @throws NotFoundException 유효하지 않은 이미지
     * @throws IOException 파일 입출력 오류
     */
    @GetMapping(path = "/objects/img/{img-name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getObjectImage(@PathVariable(name = "img-name") String imgName) throws NotFoundException, IOException {
        return submitteeObjectSignImgService.getByteByName(imgName);
    }
}












