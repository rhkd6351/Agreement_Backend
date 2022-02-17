package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.dto.project.ProjectObjectCheckboxDTO;
import com.curioud.signclass.dto.project.ProjectObjectSignDTO;
import com.curioud.signclass.dto.project.ProjectObjectTextDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.repository.project.ProjectRepository;
import com.curioud.signclass.service.user.UserFindService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectUpdateService {

    ProjectRepository projectRepository;

    UserFindService userFindService;
    PdfService pdfService;
    ProjectFindService projectFindService;


    public ProjectUpdateService(ProjectRepository projectRepository, UserFindService userFindService, PdfService pdfService, ProjectFindService projectFindService) {
        this.projectRepository = projectRepository;
        this.userFindService = userFindService;
        this.pdfService = pdfService;
        this.projectFindService = projectFindService;
    }

    @Transactional
    public ProjectDTO saveWithPdf(ProjectDTO dto, MultipartFile mf) throws AuthException, IOException, NotSupportedException {

        PdfVO savedPdf = pdfService.save(mf);
        UserVO user = userFindService.getMyUserWithAuthorities();

        ProjectVO projectVO = ProjectVO.builder()
                .user(user)
                .pdf(savedPdf)
                .name(UUID.randomUUID().toString())
                .title(savedPdf.getOriginalName().split("\\.pdf")[0])
                .description(dto.getDescription())
                .activated(1)
                .build();

        projectRepository.save(projectVO);

        return projectVO.dto(true, false, false);
    }

    @Transactional
    public ProjectDTO saveObjectsByName(ProjectDTO dto, String name) throws NotFoundException, AuthException, IllegalAccessException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        ProjectVO project = projectFindService.getByName(name);

        if(!project.ownershipCheck(user))
            throw new AuthException("not owned project name");

        if(!project.isEditable())
            throw new IllegalAccessException("Editing is not possible while sharing.");

        project.removeAllObjects();

        project.addAllObjects(dto.getProjectObjectSigns().stream().map(ProjectObjectSignDTO::toEntity).collect(Collectors.toList()));
        project.addAllObjects(dto.getProjectObjectTexts().stream().map(ProjectObjectTextDTO::toEntity).collect(Collectors.toList()));
        project.addAllObjects(dto.getProjectObjectCheckboxes().stream().map(ProjectObjectCheckboxDTO::toEntity).collect(Collectors.toList()));

        projectRepository.save(project);

        return project.dto(true, false, false);
    }

    @Transactional
    public void updateState(String name, int state) throws BadRequestException, AuthException, NotFoundException {

        ProjectVO project = projectFindService.getByName(name);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if(!project.ownershipCheck(user))
            throw new AuthException("not your own project");

        project.updateState(state);

        projectRepository.save(project);
    }

    @Transactional
    public void updateTitle(String name, String title) throws NotFoundException, AuthException, BadRequestException {

        ProjectVO project = projectFindService.getByName(name);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if(!project.ownershipCheck(user))
            throw new AuthException("not your own project");

        project.updateTitle(title);

        projectRepository.save(project);
    }

    @Transactional
    public void copyProject(String projectName) throws AuthException, NotFoundException {

        ProjectVO project = projectFindService.getByName(projectName);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if(!project.ownershipCheck(user))
            throw new AuthException("not your own project");

        ProjectVO projectVO = ProjectVO.builder()
                .user(user)
                .pdf(project.getPdf())
                .name(UUID.randomUUID().toString())
                .title(project.getTitle() + "_copy")
                .description(project.getDescription())
                .activated(1)
                .build();

        //list로 변환
        List<ProjectObjectVO> copyList = new ArrayList<>(project.getProjectObjects());

        //object 복제
        for(int i = 0; i < project.getProjectObjects().size(); i++ ){
            if(copyList.get(i) instanceof ProjectObjectCheckboxVO){
                projectVO.addObject(((ProjectObjectCheckboxVO) copyList.get(i)).cloneObject());
            }else if(copyList.get(i) instanceof ProjectObjectSignVO){
                projectVO.addObject(((ProjectObjectSignVO) copyList.get(i)).cloneObject());
            }else if(copyList.get(i) instanceof ProjectObjectTextVO) {
                projectVO.addObject(((ProjectObjectTextVO) copyList.get(i)).cloneObject());
            }
        }

        projectRepository.save(projectVO);
    }


}





