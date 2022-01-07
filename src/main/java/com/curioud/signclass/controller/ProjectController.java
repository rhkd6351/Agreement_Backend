package com.curioud.signclass.controller;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.PdfDTO;
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
     * @param mf multipart-File 형식 pdf 파일입니다.
     * @param projectDTO dto.description만 optional로 입력받습니다.
     * @return ProjectDTO를 리턴합니다.
     * @throws IOException pdf 파일를 저장하는데 오류가 발생하였습니다.
     * @throws NotSupportedException 확장자가 pdf 이외의 타입입니다.
     * @throws NotFoundException pdf 등록 후 project가 등록되는데, pdf의 name값이 유효하지 않습니다.
     * 또는 mf 변수가 비어있습니다.
     * @throws AuthException 토큰 및 권한 정보가 유효하지 않습니다.
     */
    @PostMapping("/project")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> save
            (@RequestParam("pdfFile") MultipartFile mf, ProjectDTO projectDTO) throws IOException, NotSupportedException, NotFoundException, AuthException {

        if(mf.isEmpty())
            throw new NotFoundException("Empty file"); //TODO 예외 바꾸기

        PdfVO savedPdf = pdfService.save(mf);
        PdfDTO pdfDTO = objectConverter.PdfVOToDTO(savedPdf);

        projectDTO.setPdf(pdfDTO);
        ProjectVO projectVO = projectService.save(projectDTO);
        ProjectDTO resultDTO = objectConverter.ProjectVOToDTOWithUserAndPdf(projectVO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     *
     * @return List:ProjectDTO 토큰 계정 소유 프로젝트 리스트를 반환
     * @throws AuthException  토큰 및 권한 정보가 유효하지 않습니다.
     */
    @GetMapping("/projects")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ProjectDTO>> getMyProjects() throws AuthException {

        List<ProjectVO> projects = projectService.getMyProjects();
        List<ProjectDTO> projectDTOs = projects.stream().map(objectConverter::ProjectVOToDTOWithSubmittees).collect(Collectors.toList());

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }
}












