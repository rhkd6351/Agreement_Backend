package com.curioud.signclass.controller;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.service.project.ProjectService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProjectController {

    PdfService pdfService;
    ProjectService projectService;
    ObjectConverter objectConverter;

    public ProjectController(PdfService pdfService, ProjectService projectService, ObjectConverter objectConverter) {
        this.pdfService = pdfService;
        this.projectService = projectService;
        this.objectConverter = objectConverter;
    }

    /**
     *
     * @param mf pdf
     * @param projectDTO
     * description 프로젝트 설명
     *
     * @return 등록된 프로젝트
     * @throws IOException pdf file IO 오류
     * @throws NotSupportedException pdf 이외의 확장자
     * @throws NotFoundException 유효하지 않은 pdf name
     * 또는 mf 변수가 비어있습니다.
     * @throws AuthException 유효하지 않은 토큰정보
     */
    @PostMapping("/project")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> save
            (@RequestParam("file_pdf") MultipartFile mf, ProjectDTO projectDTO) throws IOException, NotSupportedException, NotFoundException, AuthException {

        ProjectVO projectVO = projectService.saveWithPdf(projectDTO, mf);
        ProjectDTO resultDTO = objectConverter.projectVOToDTOWithUserAndPdf(projectVO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     *
     * @return
     * List:ProjectDTO 소유 프로젝트 리스트
     * @throws AuthException 유효하지 않은 토큰정보
     */
    @GetMapping("/projects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ProjectDTO>> getMyProjects() throws AuthException {

        List<ProjectVO> projects = projectService.getMyProjects();
        List<ProjectDTO> projectDTOs = projects.stream().map(objectConverter::projectVOToDTOWithSubmittees).collect(Collectors.toList());

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    /**
     *
     * @param projectName 프로젝트 이름
     * @return 프로젝트 (objects, pdf, submittee 가 포함된)
     * @throws AuthException 소유하지 않은 프로젝트
     * @throws NotFoundException
     */
    @GetMapping("/project/{project-name}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable(value = "project-name")String projectName) throws AuthException, NotFoundException {

        ProjectDTO projectDTO = projectService.getWithSubmitteesAndProjectObjectsAndPdfByName(projectName);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    /**
     *
     * @param projectDTO
     * name 프로젝트 이름
     * List:ProjectObjectTextVO Text 오브젝트 리스트
     * List:ProjectObjectCheckboxVO Checkbox 오브젝트 리스트
     * List:ProjectObjectSignVO Sign 오브젝트 리스트
     * @return 프로젝트와 등록된 오브젝트
     * @throws NotFoundException 유효하지 않은 프로젝트 이름
     * @throws AuthException 소유하지 않은 프로젝트
     * @throws IllegalAccessException 이미 작성된 프로젝트
     */
    @PostMapping("/project/{project-name}/objects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> saveObjects(
            @RequestBody ProjectDTO projectDTO, @PathVariable("project-name") String projectName) throws NotFoundException, AuthException, IllegalAccessException {

        projectDTO.setName(projectName);
        ProjectDTO resultProjectDTO = projectService.saveObjects(projectDTO);

        return new ResponseEntity<>(resultProjectDTO, HttpStatus.OK);
    }



}












