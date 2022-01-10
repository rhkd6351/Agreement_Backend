package com.curioud.signclass.controller;

import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.service.project.ProjectService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SubmitteeController {


    @Autowired
    ProjectService projectService;


    /**
     *
     * @param projectName project name
     * @return project with objects, pdf
     * @throws NotFoundException 유효하지 않은 project name
     */
    @GetMapping("/submittee/project/{project-name}")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable("project-name")String projectName) throws NotFoundException {

        ProjectDTO projectDTO = projectService.getWithProjectObjectsAndPdfByName(projectName);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }



}












