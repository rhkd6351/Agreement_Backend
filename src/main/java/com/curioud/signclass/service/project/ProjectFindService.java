package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.*;
import com.curioud.signclass.repository.project.ProjectRepository;
import com.curioud.signclass.service.user.UserFindService;
import com.curioud.signclass.util.FileUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.security.auth.message.AuthException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectFindService {

    ProjectRepository projectRepository;

    UserFindService userFindService;
    PdfService pdfService;
    FileUtil fileUtil;

    public ProjectFindService(ProjectRepository projectRepository, UserFindService userFindService, PdfService pdfService, FileUtil fileUtil) {
        this.projectRepository = projectRepository;
        this.userFindService = userFindService;
        this.pdfService = pdfService;
        this.fileUtil = fileUtil;
    }

    @Transactional(readOnly = true)
    public ProjectVO getByName(String name) throws NotFoundException {
        Optional<ProjectVO> optional = projectRepository.findOneByName(name);

        if (optional.isEmpty())
            throw new NotFoundException("invalid project Name");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public PagingProjectDTO getPageByAuth(Pageable pageable) throws AuthException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        Page<ProjectVO> projects = projectRepository.findWithSubmitteesByUser(user, pageable);

        List<ProjectDTO> projectDTOList = projects.stream().map(i -> i.dto(false, false, false)).collect(Collectors.toList());

        return PagingProjectDTO.builder()
                .projects(projectDTOList)
                .totalPage(projects.getTotalPages() - 1)
                .currentPage(pageable.getPageNumber())
                .build();
    }

    @Transactional(readOnly = true)
    public ProjectDTO getWithProjectObjectsAndPdfByName(String name) throws AuthException, NotFoundException, IOException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        ProjectVO project = projectRepository.findWithSubmitteesAndProjectObjectsAndPdfByName(name);

        if(!project.ownershipCheck(user))
            throw new AuthException("not owned project name");

        ProjectDTO projectDTO = project.dto(true, false, true);

        //pdf 원본 width 길이 배열로 반환
        float[] originalWidthArray = fileUtil.getOriginalWidthArray(project.getPdf());
        projectDTO.getPdf().setOriginalWidth(originalWidthArray);

        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectDTO getWithProjectObjectsAndPdfByNameWithoutAuth(String name) throws NotFoundException, IOException {

        ProjectVO project = projectRepository.findWithProjectObjectsAndPdfByName(name);

        if(project.getActivated() != 2)
            throw new NotAcceptableStatusException("project is not public");

        ProjectDTO projectDTO = project.dto(true, false, true);

        //pdf 원본 width 길이 배열로 반환
        float[] originalWidthArray = fileUtil.getOriginalWidthArray(project.getPdf());
        projectDTO.getPdf().setOriginalWidth(originalWidthArray);

        return projectDTO;
    }

}