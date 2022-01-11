package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.*;
import com.curioud.signclass.repository.project.ProjectRepository;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService {

    ProjectRepository projectRepository;

    UserService userService;
    PdfService pdfService;
    ProjectObjectCheckboxService projectObjectCheckboxService;
    ProjectObjectSignService projectObjectSignService;
    ProjectObjectTextService projectObjectTextService;
    ObjectConverter objectConverter;


    public ProjectService(ProjectRepository projectRepository, UserService userService, PdfService pdfService, ProjectObjectCheckboxService projectObjectCheckboxService, ProjectObjectSignService projectObjectSignService, ProjectObjectTextService projectObjectTextService, ObjectConverter objectConverter) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.pdfService = pdfService;
        this.projectObjectCheckboxService = projectObjectCheckboxService;
        this.projectObjectSignService = projectObjectSignService;
        this.projectObjectTextService = projectObjectTextService;
        this.objectConverter = objectConverter;
    }

    @Transactional
    public ProjectVO save(ProjectVO vo) {
        return projectRepository.save(vo);
    }

    @Transactional
    public ProjectVO saveWithPdf(ProjectDTO dto, MultipartFile mf) throws AuthException, IOException, NotSupportedException {

        PdfVO savedPdf = pdfService.save(mf);
        ProjectVO projectVO;

        if (dto.getIdx() == null) {

            UserVO user = userService.getMyUserWithAuthorities();

            projectVO = ProjectVO.builder()
                    .user(user)
                    .pdf(savedPdf)
                    .name(UUID.randomUUID().toString())
                    .title(savedPdf.getOriginalName().split("\\.pdf")[0]) //TODO 수정할것
                    .description(dto.getDescription())
                    .activated(0) // 0 -> 생성됨
                    .build();

        } else
            throw new UnsupportedOperationException("you can't update exist project");

        this.save(projectVO);

        return projectVO;

    }

    @Transactional
    public ProjectDTO saveObjects(ProjectDTO dto) throws NotFoundException, AuthException, IllegalAccessException {

        UserVO user = userService.getMyUserWithAuthorities();

        ProjectVO project = this.getByName(dto.getName());

        if(project.getActivated() != 0)
            throw new IllegalAccessException("already written project");

        if(project.getUser() != user)
            throw new AuthException("not owned project name");

        project.setActivated(1);
        this.save(project);

        ProjectDTO projectDTO = objectConverter.projectVOToDTO(project);;

        for(ProjectObjectSignDTO sign : dto.getProjectObjectSigns()){
            ProjectObjectSignVO save = projectObjectSignService.save(sign, project);
            projectDTO.getProjectObjectSigns().add(objectConverter.projectObjectSignVOToDTO(save));
        }

        for(ProjectObjectCheckboxDTO checkbox : dto.getProjectObjectCheckboxes()){
            ProjectObjectCheckboxVO save = projectObjectCheckboxService.save(checkbox, project);
            projectDTO.getProjectObjectCheckboxes().add(objectConverter.projectObjectCheckboxVOToDTO(save));
        }

        for(ProjectObjectTextDTO text : dto.getProjectObjectTexts()){
            ProjectObjectTextVO save = projectObjectTextService.save(text, project);
            projectDTO.getProjectObjectTexts().add(objectConverter.projectObjectTextVOToDTO(save));
        }

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectDTO getWithSubmitteesAndProjectObjectsAndPdfByName(String name) throws AuthException, NotFoundException {

        UserVO user = userService.getMyUserWithAuthorities();
        ProjectVO project = projectRepository.findWithSubmitteesAndProjectObjectsAndPdfByName(name);

        if(user != project.getUser())
            throw new AuthException("not owned project name");

        ProjectDTO projectDTO = objectConverter.projectVOToDTO(project);

        Set<ProjectObjectVO> projectObjects = project.getProjectObjects();
        for (ProjectObjectVO em: projectObjects) {
            switch (em.getObjectType().getName()){
                case "OBJECT_TYPE_SIGN":
                    ProjectObjectSignVO signEm = projectObjectSignService.getByIdx(em.getIdx());
                    projectDTO.getProjectObjectSigns().add(objectConverter.projectObjectSignVOToDTO(signEm));
                    break;
                case "OBJECT_TYPE_CHECKBOX":
                    ProjectObjectCheckboxVO checkboxEm = projectObjectCheckboxService.getByIdx(em.getIdx());
                    projectDTO.getProjectObjectCheckboxes().add(objectConverter.projectObjectCheckboxVOToDTO(checkboxEm));
                    break;
                case "OBJECT_TYPE_TEXT":
                    ProjectObjectTextVO textEm = projectObjectTextService.getByIdx(em.getIdx());
                    projectDTO.getProjectObjectTexts().add(objectConverter.projectObjectTextVOToDTO(textEm));
                    break;
            }
        }
        projectDTO.setPdf(objectConverter.pdfVOToDTO(project.getPdf()));
        projectDTO.getSubmittees().addAll(project.getSubmittees().stream().map(objectConverter::submitteeVOToDTO).collect(Collectors.toList()));

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectDTO getWithProjectObjectsAndPdfByName(String name) throws NotFoundException {

        ProjectVO project = projectRepository.findWithProjectObjectsAndPdfByName(name);

        if(project.getActivated() != 1)
            throw new NotAcceptableStatusException("project is not public");

        ProjectDTO projectDTO = objectConverter.projectVOToDTO(project);

        Set<ProjectObjectVO> projectObjects = project.getProjectObjects();
        for (ProjectObjectVO em: projectObjects) {
            switch (em.getObjectType().getName()){
                case "OBJECT_TYPE_SIGN":
                    ProjectObjectSignVO signEm = projectObjectSignService.getByIdx(em.getIdx());
                    projectDTO.getProjectObjectSigns().add(objectConverter.projectObjectSignVOToDTO(signEm));
                    break;
                case "OBJECT_TYPE_CHECKBOX":
                    ProjectObjectCheckboxVO checkboxEm = projectObjectCheckboxService.getByIdx(em.getIdx());
                    projectDTO.getProjectObjectCheckboxes().add(objectConverter.projectObjectCheckboxVOToDTO(checkboxEm));
                    break;
                case "OBJECT_TYPE_TEXT":
                    ProjectObjectTextVO textEm = projectObjectTextService.getByIdx(em.getIdx());
                    projectDTO.getProjectObjectTexts().add(objectConverter.projectObjectTextVOToDTO(textEm));
                    break;
            }
        }
        projectDTO.setPdf(objectConverter.pdfVOToDTO(project.getPdf()));

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectVO getByName(String name) throws NotFoundException {
        Optional<ProjectVO> optional = projectRepository.findOneByName(name);

        if (optional.isEmpty())
            throw new NotFoundException("invalid project Name");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public ProjectVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectVO> optional = projectRepository.findById(idx);

        if (optional.isEmpty())
            throw new NotFoundException("invalid project idx");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public List<ProjectVO> getMyProjects() throws AuthException {

        UserVO user = userService.getMyUserWithAuthorities();
        return projectRepository.findWithSubmitteesByUser(user);
    }

}