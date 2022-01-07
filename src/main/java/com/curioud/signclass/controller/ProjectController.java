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

    @PostMapping("/project")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProjectDTO> save
            (@RequestParam("pdfFile") MultipartFile mf, ProjectDTO projectDTO) throws IOException, NotSupportedException, NotFoundException, AuthException {

        PdfVO savedPdf = pdfService.save(mf);
        PdfDTO pdfDTO = objectConverter.PdfVOToDTO(savedPdf);

        projectDTO.setPdf(pdfDTO);
        ProjectVO projectVO = projectService.save(projectDTO);
        ProjectDTO resultDTO = objectConverter.ProjectVOToDTO(projectVO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }


}












