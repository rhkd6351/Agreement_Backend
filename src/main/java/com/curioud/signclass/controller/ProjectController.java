package com.curioud.signclass.controller;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.etc.MessageDTO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.service.project.ProjectService;
import com.curioud.signclass.service.submittee.SubmitteeService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
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
@RequestMapping("/api")
public class ProjectController {

    PdfService pdfService;
    ProjectService projectService;
    SubmitteeService submitteeService;
    ObjectConverter objectConverter;

    public ProjectController(PdfService pdfService, ProjectService projectService, ObjectConverter objectConverter, SubmitteeService submitteeService) {
        this.pdfService = pdfService;
        this.projectService = projectService;
        this.objectConverter = objectConverter;
        this.submitteeService = submitteeService;
    }

    /** Post Project
     *
     * @param mf pdf 파일
     * @param projectDTO project의 description (optional)
     * @return 저장된 project 정보
     * @throws IOException 파일 입출력 오류
     * @throws NotSupportedException 승인되지 않은 파일 확장자
     * @throws AuthException 유효하지 않은 토큰
     */
    @PostMapping("/projects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> insertProject
            (@RequestParam("file_pdf") MultipartFile mf, ProjectDTO projectDTO) throws IOException, NotSupportedException, AuthException {

        ProjectVO projectVO = projectService.saveWithPdf(projectDTO, mf);
        ProjectDTO resultDTO = objectConverter.projectVOToDTOWithPdf(projectVO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /** Get Project List
     *
     * @return 나의 프로젝트 리스트
     * @throws AuthException 유효하지 않은 토큰
     */
    @GetMapping("/projects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ProjectDTO>> getProjects() throws AuthException {

        List<ProjectDTO> projectDTOs = projectService.getMyProjects();

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    /** Get Project
     *
     * @param projectName 프로젝트 이름
     * @return 프로젝트 정보, object 및 pdf 정보 포함
     * @throws AuthException 소유하지 않은 프로젝트, 유효하지 않은 토큰
     * @throws NotFoundException 유효하지 않은 object 및 pdf (DB)
     * @throws IOException 파일 입출력 오류
     */
    @GetMapping("/projects/{project-name}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable(value = "project-name")String projectName) throws AuthException, NotFoundException, IOException {

//        ProjectDTO projectDTO = projectService.getWithSubmitteesAndProjectObjectsAndPdfByName(projectName);


        ProjectDTO projectDTO = projectService.getWithProjectObjectsAndPdfByName(projectName);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    /** Post Objects
     *
     * @param projectDTO 저장할 오브젝트 리스트
     * @param projectName 오브젝트를 저장할 프로젝트 이름
     * @return 오브젝트가 저장된 프로젝트 정보
     * @throws NotFoundException 유효하지 않은 프로젝트 이름
     * @throws AuthException 유효하지 않은 토큰, 소유하지 않은 프로젝트
     * @throws IllegalAccessException 수정이 불가능한 프로젝트에 오브젝트 저장 시도
     * @throws BadRequestException 같은 상태로의 변경 시도
     */
    @PostMapping("/projects/{project-name}/objects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> insertObjects(
            @RequestBody ProjectDTO projectDTO, @PathVariable("project-name") String projectName) throws NotFoundException, AuthException, IllegalAccessException, BadRequestException {

        projectDTO.setName(projectName);
        ProjectDTO resultProjectDTO = projectService.saveObjects(projectDTO);

        return new ResponseEntity<>(resultProjectDTO, HttpStatus.OK);
    }

    /** Change State
     *
     * @param state 변경하고자 하는 상태
     * @param projectName 프로젝트 이름
     * @return 변경 성공 여부 메시지
     * @throws NotFoundException 유효하지 않은 프로젝트 이름
     * @throws AuthException 소유하지 않은 프로젝트
     * @throws BadRequestException 잘못된 상태로의 변경시도
     */
    @PutMapping("/projects/{project-name}/state")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageDTO> updateProjectState(
            @RequestParam int state,
            @PathVariable("project-name")String projectName) throws NotFoundException, AuthException, BadRequestException {

        ProjectVO project = projectService.getByName(projectName);
        projectService.changeState(project, state);

        return new ResponseEntity<>(new MessageDTO("state changed"), HttpStatus.OK);

    }

    /** Get Submittee PDF
     *
     * @param submitteeName 제출자 이름
     * @return 제출된 프로젝트 pdf
     * @throws NotFoundException 유효하지 않은 제출자 이름, 유효하지 않은 제출자의 pdf 이름
     * @throws AuthException 유효하지 않은 토큰, 소유하지 않은 프로젝트의 제출자
     * @throws IOException 파일 입출력 오류
     */
    @GetMapping(path = "/projects/submittees/{submittee-name}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] getSubmitteePdf(@PathVariable("submittee-name")String submitteeName) throws NotFoundException, AuthException, IOException {

        return submitteeService.getSubmitteePdfFileByNameWithAuthority(submitteeName);
    }

    /** Get Submittees
     *
     * @param projectName 프로젝트의 이름
     * @return 제출자 리스트
     * @throws NotFoundException 유효하지 않은 프로젝트 이름
     * @throws AuthException 유효하지 않은 프로젝트, 소유하지 않은 프로젝트
     */
    @GetMapping("/projects/{project-name}/submittees")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<SubmitteeDTO> getSubmitteesByProjectName(
            @PathVariable("project-name") String projectName) throws NotFoundException, AuthException {

        return projectService.getSubmitteesByProjectName(projectName);
    }

    /** Get Submittee
     *
     * @param name 제출자 이름
     * @return 제출자 정보(pdf, object 포함)
     * @throws AuthException 소유하지 않은 프로젝트의 제출자, 유효하지 않은 토큰
     * @throws NotFoundException 유효하지 않은 pdf 혹은 object 혹은 submittee
     * @throws IOException pdf file 입출력 오류
     */
    @GetMapping("/projects/submittees/{submittee-name}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<SubmitteeDTO> getSubmitteeWithPdfAndObjectsByName(@PathVariable("submittee-name") String name) throws AuthException, NotFoundException, IOException {

        SubmitteeDTO dto = submitteeService.getWithPdfAndObjectsByName(name);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }



}












