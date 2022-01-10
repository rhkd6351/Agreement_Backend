package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.*;
import com.curioud.signclass.repository.project.ProjectRepository;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
    public ProjectVO saveWithPdf(ProjectDTO dto, MultipartFile mf) throws NotFoundException, AuthException, IOException, NotSupportedException {

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
            sign.setProject(projectDTO);
            ProjectObjectSignVO save = projectObjectSignService.save(sign, project);
            projectDTO.getProjectObjectSigns().add(objectConverter.projectObjectSignVOToDTO(save));
        }

        for(ProjectObjectCheckboxDTO checkbox : dto.getProjectObjectCheckboxes()){
            checkbox.setProject(projectDTO);
            ProjectObjectCheckboxVO save = projectObjectCheckboxService.save(checkbox, project);
            projectDTO.getProjectObjectCheckboxes().add(objectConverter.projectObjectCheckboxVOToDTO(save));
        }

        for(ProjectObjectTextDTO text : dto.getProjectObjectTexts()){
            text.setProject(projectDTO);
            ProjectObjectTextVO save = projectObjectTextService.save(text, project);
            projectDTO.getProjectObjectTexts().add(objectConverter.projectObjectTextVOToDTO(save));
        }

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




