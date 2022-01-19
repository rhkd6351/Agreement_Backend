package com.curioud.signclass.service.submittee;


import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.dto.submittee.*;
import com.curioud.signclass.repository.submittee.SubmitteeRepository;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.service.project.ProjectFindService;
import com.curioud.signclass.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmitteeFindService {

    SubmitteeRepository submitteeRepository;

    SubmitteeObjectSignService submitteeObjectSignService;
    SubmitteeObjectTextService submitteeObjectTextService;
    SubmitteeObjectCheckboxService submitteeObjectCheckboxService;

    ProjectFindService projectFindService;
    UserFindService userFindService;
    SubmitteePdfService submitteePdfService;
    PdfService pdfService;

    public SubmitteeFindService(SubmitteeRepository submitteeRepository, SubmitteeObjectSignService submitteeObjectSignService, SubmitteeObjectTextService submitteeObjectTextService, SubmitteeObjectCheckboxService submitteeObjectCheckboxService, ProjectFindService projectFindService, UserFindService userFindService, SubmitteePdfService submitteePdfService, PdfService pdfService) {
        this.submitteeRepository = submitteeRepository;
        this.submitteeObjectSignService = submitteeObjectSignService;
        this.submitteeObjectTextService = submitteeObjectTextService;
        this.submitteeObjectCheckboxService = submitteeObjectCheckboxService;
        this.projectFindService = projectFindService;
        this.userFindService = userFindService;
        this.submitteePdfService = submitteePdfService;
        this.pdfService = pdfService;
    }

    @Transactional(readOnly = true)
    public byte[] getSubmitteePdfFileByNameWithAuthority(String name) throws NotFoundException, AuthException, IOException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        SubmitteeVO submittee = this.getByName(name);
        if(submittee.getProject().getUser() != user)
            throw new AuthException("it's not your own project's submittee");

        return submitteePdfService.getByteByName(submittee.getSubmitteePdf().getName());
    }

    @Transactional(readOnly = true)
    public SubmitteeDTO getWithPdfAndObjectsByName(String name) throws AuthException, NotFoundException, IOException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        SubmitteeVO submittee = this.getByName(name);
        SubmitteeDTO submitteeDTO = submittee.dto();

        if(submittee.getProject().getUser() != user)
            throw new AuthException("not your own submittee");

        PdfVO pdfVO = submittee.getProject().getPdf();
        PdfDTO pdfDTO = pdfVO.dto();

        float[] originalWidthArray = pdfService.getOriginalWidthArray(pdfVO);
        pdfDTO.setOriginalWidth(originalWidthArray);

        submitteeDTO.setPdf(pdfDTO);

        List<SubmitteeObjectVO> submitteeObjects = submittee.getSubmitteeObjects();
        for(SubmitteeObjectVO object : submitteeObjects){
            switch(object.getObjectType().getName()){
                case "OBJECT_TYPE_SIGN":
                    SubmitteeObjectSignVO signEm = submitteeObjectSignService.getByIdx(object.getIdx());
                    submitteeDTO.getSubmitteeObjectSigns().add(signEm.dto());
                    break;
                case "OBJECT_TYPE_CHECKBOX":
                    SubmitteeObjectCheckboxVO checkboxEm = submitteeObjectCheckboxService.getByIdx(object.getIdx());
                    submitteeDTO.getSubmitteeObjectCheckboxes().add(checkboxEm.dto());
                    break;
                case "OBJECT_TYPE_TEXT":
                    SubmitteeObjectTextVO textEm = submitteeObjectTextService.getByIdx(object.getIdx());
                    submitteeDTO.getSubmitteeObjectTexts().add(textEm.dto());
                    break;
            }
        }
        return submitteeDTO;
    }

    @Transactional(readOnly = true)
    public SubmitteeVO getByName(String name) throws NotFoundException {
        Optional<SubmitteeVO> optional = submitteeRepository.findByName(name);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submittee name");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public PagingSubmitteeDTO getByProjectName(String projectName, Pageable pageable) throws NotFoundException, AuthException {

        ProjectVO project = projectFindService.getByName(projectName);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if(project.getUser() != user)
            throw new AuthException("not your own project");

        Page<SubmitteeVO> submitteePage = submitteeRepository.getByProject(project, pageable);

        return PagingSubmitteeDTO.builder()
                .submittees(submitteePage.stream().map(SubmitteeVO::dto).collect(Collectors.toList()))
                .totalPage(submitteePage.getTotalPages() - 1)
                .currentPage(pageable.getPageNumber())
                .build();

    }
}
