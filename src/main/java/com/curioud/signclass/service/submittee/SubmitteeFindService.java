package com.curioud.signclass.service.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.SubmitteePdfVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.submittee.PagingSubmitteeDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.repository.submittee.SubmitteeRepository;
import com.curioud.signclass.service.project.ProjectFindService;
import com.curioud.signclass.service.user.UserFindService;
import com.curioud.signclass.util.FileUtil;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmitteeFindService {

    SubmitteeRepository submitteeRepository;

    ProjectFindService projectFindService;
    UserFindService userFindService;
    SubmitteePdfService submitteePdfService;
    FileUtil fileUtil;


    public SubmitteeFindService(SubmitteeRepository submitteeRepository, ProjectFindService projectFindService, UserFindService userFindService, SubmitteePdfService submitteePdfService, FileUtil fileUtil) {
        this.submitteeRepository = submitteeRepository;
        this.projectFindService = projectFindService;
        this.userFindService = userFindService;
        this.submitteePdfService = submitteePdfService;
        this.fileUtil = fileUtil;
    }

    @Transactional(readOnly = true)
    public SubmitteeVO getByName(String name) throws NotFoundException {
        Optional<SubmitteeVO> optional = submitteeRepository.findByName(name);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submittee name");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public PagingSubmitteeDTO getPageByProjectName(String projectName, Pageable pageable) throws NotFoundException, AuthException {

        ProjectVO project = projectFindService.getByName(projectName);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if(!project.ownershipCheck(user))
            throw new AuthException("not your own project");

        Page<SubmitteeVO> submitteePage = submitteeRepository.getByProject(project, pageable);

        return PagingSubmitteeDTO.builder()
                .submittees(submitteePage.stream().map(i -> i.dto(false, null)).collect(Collectors.toList()))
                .totalPage(submitteePage.getTotalPages() - 1) //TODO totalpage.. 관리 어떻게할지 고민
                .currentPage(pageable.getPageNumber())
                .build();

    }

    @Transactional(readOnly = true)
    public byte[] getSubmitteePdfByName(String name, Boolean authCheck) throws NotFoundException, AuthException, IOException {

        SubmitteeVO submittee = this.getByName(name);
        SubmitteePdfVO submitteePdfVO = submittee.getSubmitteePdf();

        if(authCheck && !submittee.getProject().ownershipCheck(userFindService.getMyUserWithAuthorities()))
            throw new AuthException("it's not your own project's submittee");

        if(submitteePdfVO == null)
            throw new NotFoundException("there's no pdf file connected to submittee");

        String fileName = submittee.getProject().getName() + "_" + submittee.getStudentName() + "_" + submittee.getRegDate();

        return fileUtil.getFile(submitteePdfVO, fileName);
    }

    @Transactional(readOnly = true)
    public SubmitteeDTO getWithPdfAndObjectsByName(String name) throws AuthException, NotFoundException, IOException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        SubmitteeVO submittee = this.getByName(name);

        if(!submittee.getProject().ownershipCheck(user))
            throw new AuthException("not your own project");

        SubmitteeDTO submitteeDTO = submittee.dto(true, submittee.getProject().getPdf());
        float[] originalWidthArray = fileUtil.getOriginalWidthArray(submittee.getProject().getPdf());
        submitteeDTO.getPdf().setOriginalWidth(originalWidthArray);

        return submitteeDTO;
    }
}
