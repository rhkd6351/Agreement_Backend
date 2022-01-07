package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.ProjectDTO;
import com.curioud.signclass.repository.project.ProjectRepository;
import com.curioud.signclass.service.user.UserService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    ProjectRepository projectRepository;

    UserService userService;
    PdfService pdfService;
//    ProjectObjectCheckboxService projectObjectCheckboxService;
//    ProjectObjectSignService projectObjectSignService;
//    ProjectObjectTextService projectObjectTextService;


    public ProjectService(ProjectRepository projectRepository, UserService userService, PdfService pdfService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.pdfService = pdfService;
    }

    public ProjectVO save(ProjectVO vo) {
        return projectRepository.save(vo);
    }

    @Transactional
    public ProjectVO save(ProjectDTO dto) throws NotFoundException, AuthException {
        ProjectVO projectVO;

        if (dto.getIdx() == null) {

            UserVO user = userService.getMyUserWithAuthorities();
            PdfVO pdf = pdfService.getByName(dto.getPdf().getName());

            projectVO = ProjectVO.builder()
                    .user(user)
                    .pdf(pdf)
                    .name(UUID.randomUUID().toString())
                    .title(pdf.getOriginalName().split("\\.")[0]) //TODO 수정할것
//                    .description(" ")
                    .description(dto.getDescription())
                    .activated(true)
                    .build();

        } else {
            projectVO = this.getByIdx(dto.getIdx());
//            projectVO.setName(dto.getName()); 이름 수정불가
            projectVO.setTitle(dto.getTitle());
            projectVO.setDescription(dto.getDescription());
            projectVO.setUpDate(LocalDateTime.now());
            projectVO.setActivated(dto.isActivated());
//            projectVO.setPdf(dto.getPdf()); pdf 수정못함
//            projectVO.setUser(user); 유저 수정못함
        }

        this.save(projectVO);

        return projectVO;

    }

//    @Transactional
//    public ProjectVO saveWithObjects(ProjectDTO dto) throws NotFoundException, AuthException {
//
//        ProjectVO project = this.save(dto);
//        ProjectDTO idxInsertDTO = ProjectDTO.builder().idx(dto.getIdx()).build();
//
//        for(ProjectObjectSignDTO sign : dto.getProjectObjectSigns()){
//            sign.setProject(idxInsertDTO);
//            projectObjectSignService.save(sign);
//        }
//
//        for(ProjectObjectCheckboxDTO checkbox : dto.getProjectObjectCheckboxes()){
//            checkbox.setProject(idxInsertDTO);
//            projectObjectCheckboxService.save(checkbox);
//        }
//
//        for(ProjectObjectTextDTO text : dto.getProjectObjectTexts()){
//            text.setProject(idxInsertDTO);
//            projectObjectTextService.save(text);
//        }
//
//        return project;
//    }

    public ProjectVO getByName(String name) throws NotFoundException {
        Optional<ProjectVO> optional = projectRepository.findOneByName(name);

        if (optional.isEmpty())
            throw new NotFoundException("invalid project Name");

        return optional.get();
    }

    public ProjectVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectVO> optional = projectRepository.findById(idx);

        if (optional.isEmpty())
            throw new NotFoundException("invalid project idx");

        return optional.get();
    }

}




