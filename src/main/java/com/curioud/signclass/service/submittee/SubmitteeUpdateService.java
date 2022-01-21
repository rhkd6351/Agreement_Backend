package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.*;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectCheckboxDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectTextDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.repository.submittee.SubmitteeRepository;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.service.project.ProjectFindService;
import com.curioud.signclass.service.user.UserFindService;
import com.curioud.signclass.util.FileUtil;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubmitteeUpdateService {

    SubmitteeRepository submitteeRepository;
    SubmitteeObjectSignImgService submitteeObjectSignImgService;

    ProjectFindService projectFindService;
    SubmitteePdfService submitteePdfService;

    public SubmitteeUpdateService(SubmitteeRepository submitteeRepository, SubmitteeObjectSignImgService submitteeObjectSignImgService, ProjectFindService projectFindService, SubmitteePdfService submitteePdfService) {
        this.submitteeRepository = submitteeRepository;
        this.submitteeObjectSignImgService = submitteeObjectSignImgService;
        this.projectFindService = projectFindService;
        this.submitteePdfService = submitteePdfService;
    }

    @Transactional
    public SubmitteeDTO saveWithObjectsAndPdf(String projectName, SubmitteeDTO dto, List<MultipartFile> mfList, MultipartFile pdf) throws NotFoundException, IOException, NotSupportedException, BadRequestException {

        //project 활성화 여부 확인
        ProjectVO project = projectFindService.getByName(projectName);
        if(!project.isPublished()) throw new NotAcceptableStatusException("project is closed");

        //이미지 갯수, sign 오브젝트 갯수 비교
        if(mfList.size() != dto.getSubmitteeObjectSigns().size()) throw new BadRequestException("img size and sign objects size should be same");

        //이미지 이름 동일여부 확인
        for(int i = 0; i < mfList.size(); i++){
            MultipartFile imf = mfList.get(i);
            int p = Objects.requireNonNull(imf.getOriginalFilename()).lastIndexOf(".");
            String iFileName = imf.getOriginalFilename().substring(0, p);
            for(int k = i + 1; k < mfList.size(); k++){
                MultipartFile kmf = mfList.get(k);
                int t = Objects.requireNonNull(kmf.getOriginalFilename()).lastIndexOf(".");
                String kFileName = kmf.getOriginalFilename().substring(0, t);

                if(iFileName.equals(kFileName)) throw new BadRequestException("file name should be different to each other");
            }
        }

        SubmitteePdfVO savedPdf = submitteePdfService.save(pdf);
        SubmitteeVO submitteeVO = dto.toEntity(project, savedPdf);

        //이미지 파일 이름과 sign object 이름의 일치 확인, 일치하면 object 저장
        for(SubmitteeObjectSignDTO signDTO : dto.getSubmitteeObjectSigns()){
            boolean isPresent = false;
            for(MultipartFile mf : mfList){
                if(signDTO.isNameEqual(mf)){
                    SubmitteeObjectSignImgVO savedImgVO = submitteeObjectSignImgService.save(mf);
                    submitteeVO.addObject(signDTO.toEntity(savedImgVO));
                    isPresent = true;
                    break;
                }
            }

            if(!isPresent)
                throw new BadRequestException("there's no connection between file and data");
        }

        submitteeVO.addAllObjects(dto.getSubmitteeObjectTexts().stream().map(SubmitteeObjectTextDTO::toEntity).collect(Collectors.toList()));
        submitteeVO.addAllObjects(dto.getSubmitteeObjectCheckboxes().stream().map(SubmitteeObjectCheckboxDTO::toEntity).collect(Collectors.toList()));

        submitteeRepository.save(submitteeVO);

        return submitteeVO.dto(false, null, true);
    }

}
