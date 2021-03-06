package com.curioud.signclass.controller;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.etc.MessageDTO;
import com.curioud.signclass.dto.project.PagingProjectDTO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.submittee.PagingSubmitteeDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.service.project.ProjectFindService;
import com.curioud.signclass.service.project.ProjectUpdateService;
import com.curioud.signclass.service.submittee.SubmitteeFindService;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class ProjectController {

    PdfService pdfService;
    ProjectFindService projectFindService;
    ProjectUpdateService projectUpdateService;
    SubmitteeFindService submitteeFindService;

    public ProjectController(PdfService pdfService, ProjectFindService projectFindService, ProjectUpdateService projectUpdateService, SubmitteeFindService submitteeFindService) {
        this.pdfService = pdfService;
        this.projectFindService = projectFindService;
        this.projectUpdateService = projectUpdateService;
        this.submitteeFindService = submitteeFindService;
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
            (@RequestParam(value = "file_pdf") MultipartFile mf, ProjectDTO projectDTO)
            throws IOException, NotSupportedException, AuthException {

        ProjectDTO project = projectUpdateService.saveWithPdf(projectDTO, mf);

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    /** Get Project List
     *
     * @return 나의 프로젝트 리스트
     * @throws AuthException 유효하지 않은 토큰
     */
    @GetMapping("/projects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PagingProjectDTO> getProjects(@PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable)
            throws AuthException {

        PagingProjectDTO pagingProjectDTO = projectFindService.getPageByAuth(pageable);

        return new ResponseEntity<>(pagingProjectDTO, HttpStatus.OK);
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

        ProjectDTO projectDTO = projectFindService.getWithProjectObjectsAndPdfByName(projectName);

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

        ProjectDTO resultProjectDTO = projectUpdateService.saveObjectsByName(projectDTO, projectName);

        return new ResponseEntity<>(resultProjectDTO, HttpStatus.CREATED);
    }

    /** update State
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

        projectUpdateService.updateState(projectName, state);

        return new ResponseEntity<>(new MessageDTO("state changed"), HttpStatus.OK);

    }

    @PutMapping("/projects/{project-name}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageDTO> updateProjectTitle(
            @RequestParam String title,
            @PathVariable("project-name")String projectName) throws NotFoundException, AuthException, BadRequestException {

        projectUpdateService.updateTitle(projectName, title);

        return new ResponseEntity<>(new MessageDTO("title changed"), HttpStatus.OK);
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
    public ResponseEntity<byte[]> getSubmitteePdf(@PathVariable("submittee-name")String submitteeName) throws NotFoundException, AuthException, IOException {

        SubmitteeVO submittee = submitteeFindService.getByName(submitteeName);
        ProjectVO project = submittee.getProject();

        String fileName = project.getTitle().replace(" ", "") +  "_" + submittee.getStudentId() + "_" + submittee.getStudentName() + "_" + submittee.getRegDate().format(DateTimeFormatter.ofPattern("yyMMdd_HHmmss"));
        String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        //헤더 파일명 입력
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(encodedName).build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(submitteeFindService.getSubmitteePdfByName(submitteeName, true), headers, HttpStatus.OK);
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
    public PagingSubmitteeDTO getSubmitteesByProjectName(
            @PathVariable("project-name") String projectName,
            @PageableDefault(size = 1000, sort = "idx", direction = Sort.Direction.DESC)Pageable pageable) throws NotFoundException, AuthException {

        return submitteeFindService.getPageByProjectName(projectName, pageable);
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

        SubmitteeDTO dto = submitteeFindService.getWithPdfAndObjectsByName(name);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/projects/{project-name}/copy")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<MessageDTO> copyProject(@PathVariable("project-name") String projectName) throws AuthException, NotFoundException {

        projectUpdateService.copyProject(projectName);

        return new ResponseEntity<>(new MessageDTO("copy success"), HttpStatus.CREATED);

    }



}



