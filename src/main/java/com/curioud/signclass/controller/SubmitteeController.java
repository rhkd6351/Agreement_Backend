package com.curioud.signclass.controller;

import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.service.project.ProjectService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api")
public class SubmitteeController {


    @Autowired
    ProjectService projectService;


    /**
     *
     * @param projectName
     * @return
     * @throws NotFoundException
     */
    @GetMapping("/submittee/project/{project-name}")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable("project-name")String projectName) throws NotFoundException {

        ProjectDTO projectDTO = projectService.getWithProjectObjectsAndPdfByName(projectName);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }



}












