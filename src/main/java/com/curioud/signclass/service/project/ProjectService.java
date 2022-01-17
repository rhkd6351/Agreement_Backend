package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.*;
import com.curioud.signclass.dto.submittee.PagingSubmitteeDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.repository.project.ProjectRepository;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    ProjectObjectService projectObjectService;

    public ProjectService(ProjectRepository projectRepository, UserService userService, PdfService pdfService, ProjectObjectCheckboxService projectObjectCheckboxService, ProjectObjectSignService projectObjectSignService, ProjectObjectTextService projectObjectTextService, ObjectConverter objectConverter, ProjectObjectService projectObjectService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.pdfService = pdfService;
        this.projectObjectCheckboxService = projectObjectCheckboxService;
        this.projectObjectSignService = projectObjectSignService;
        this.projectObjectTextService = projectObjectTextService;
        this.objectConverter = objectConverter;
        this.projectObjectService = projectObjectService;
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
                    .activated(1) // 0 -> 생성됨, 1-> 1회 이상 작성됨
                    // 현재 규정상 생성 즉시 공유 가능이므로 1로 생성
                    .build();

        } else
            throw new UnsupportedOperationException("you can't update exist project");

        this.save(projectVO);

        return projectVO;

    }

    @Transactional
    public ProjectDTO saveObjects(ProjectDTO dto) throws NotFoundException, AuthException, IllegalAccessException, BadRequestException {

        UserVO user = userService.getMyUserWithAuthorities();

        ProjectVO project = this.getByName(dto.getName());

        //0: 생성됨, 1: 생성 후 1회 이상 작성됨, 2: 공유됨, 3: 공유 중단됨
        if(project.getActivated() == 2 || project.getActivated() == 3) {
            throw new IllegalAccessException("Editing is not possible while sharing.");
        }
        //수정 규정 변경으로 activated가 2 또는 3이면 반드시 수정 불가
//        }else if(project.getActivated() == 3 && project.getSubmittees().size() >= 1){
//            throw new IllegalAccessException("Sharing is not allowed if there is more than one submitter.");
//        }

        //상태변경
        //1, 3일 때 수정은 상태변경이 없음
        if(project.getActivated() == 0)
            this.setActivated(project, 1);


        if(project.getUser() != user)
            throw new AuthException("not owned project name");

        Set<ProjectObjectVO> projectObjects = project.getProjectObjects();
        for(ProjectObjectVO object : projectObjects){
            log.warn(object.getName() + "삭제됨");
            projectObjectService.remove(object);
        }

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

    @Transactional
    public void changeState(ProjectVO project, int state) throws BadRequestException, AuthException {

        if(state < 0 || state > 3)
            throw new BadRequestException("state should be between 0 and 3");

        if(state == 0 || state == 1)
            throw new BadRequestException("project state can't be changed to 0 or 1");

        if(project.getActivated() == 0)
            throw new BadRequestException("unwritten project can't be shared");

        if(state == 3 && project.getActivated() == 1){
            throw new BadRequestException("only shared project can be interrupted");
        }

        this.setActivated(project, state);
    }

    @Transactional
    public void setActivated(ProjectVO vo, int state) throws AuthException, BadRequestException {

        UserVO user = userService.getMyUserWithAuthorities();

        if(vo.getUser().getIdx() != user.getIdx())
            throw new AuthException("not your own project");

        if(vo.getActivated() == state)
            throw new BadRequestException("current state and changed state are the same");

        vo.setActivated(state);
        this.save(vo);
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
        projectDTO.setSubmittees(project.getSubmittees().stream().map(objectConverter::submitteeVOToDTO).collect(Collectors.toList()));

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectDTO getWithProjectObjectsAndPdfByName(String name) throws AuthException, NotFoundException, IOException {

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
        projectDTO.setSubmitteeCount(project.getSubmittees().size());

        //pdf 원본 width 길이 배열로 반환
        float[] originalWidthArray = pdfService.getOriginalWidthArray(project.getPdf());
        projectDTO.getPdf().setOriginalWidth(originalWidthArray);

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectDTO getWithProjectObjectsAndPdfByNameWithoutAuthority(String name) throws NotFoundException, IOException {

        ProjectVO project = projectRepository.findWithProjectObjectsAndPdfByName(name);

        if(project.getActivated() != 2)
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
        float[] originalWidthArray = pdfService.getOriginalWidthArray(project.getPdf());
        projectDTO.getPdf().setOriginalWidth(originalWidthArray);

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public List<SubmitteeDTO> getSubmitteesByProjectName(String projectName) throws NotFoundException, AuthException {

        ProjectVO project = this.getByName(projectName);
        UserVO user = userService.getMyUserWithAuthorities();

        if(project.getUser() != user)
            throw new AuthException("not your own project");

        return project.getSubmittees().stream().map(objectConverter::submitteeVOToDTO).collect(Collectors.toList());

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
    public PagingProjectDTO getMyProjects(Pageable pageable) throws AuthException {

        UserVO user = userService.getMyUserWithAuthorities();
        Page<ProjectVO> projects = projectRepository.findWithSubmitteesByUser(user, pageable);

        List<ProjectDTO> projectDTOList = projects.stream().map(i -> {
            ProjectDTO dto = objectConverter.projectVOToDTO(i);
            dto.setSubmitteeCount(i.getSubmittees().size());
            return dto;
        }).collect(Collectors.toList());

        return PagingProjectDTO.builder()
                .projects(projectDTOList)
                .totalPage(projects.getTotalPages() - 1)
                .currentPage(pageable.getPageNumber())
                .build();
    }

}