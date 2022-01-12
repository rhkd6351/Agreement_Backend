package com.curioud.signclass.service.submittee;


import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.*;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectCheckboxDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectTextDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.repository.submittee.SubmitteeRepository;
import com.curioud.signclass.service.project.ProjectService;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmitteeService {

    SubmitteeRepository submitteeRepository;

    SubmitteeObjectSignService submitteeObjectSignService;
    SubmitteeObjectTextService submitteeObjectTextService;
    SubmitteeObjectCheckboxService submitteeObjectCheckboxService;
    ProjectService projectService;
    ObjectConverter objectConverter;
    UserService userService;

    public SubmitteeService(SubmitteeRepository submitteeRepository, SubmitteeObjectSignService submitteeObjectSignService, SubmitteeObjectTextService submitteeObjectTextService, SubmitteeObjectCheckboxService submitteeObjectCheckboxService, ProjectService projectService, ObjectConverter objectConverter, UserService userService) {
        this.submitteeRepository = submitteeRepository;
        this.submitteeObjectSignService = submitteeObjectSignService;
        this.submitteeObjectTextService = submitteeObjectTextService;
        this.submitteeObjectCheckboxService = submitteeObjectCheckboxService;
        this.projectService = projectService;
        this.objectConverter = objectConverter;
        this.userService = userService;
    }

    @Transactional
    public SubmitteeVO save(SubmitteeVO vo){
        return submitteeRepository.save(vo);
    }

    @Transactional
    public SubmitteeDTO saveWithObjects(String projectName, SubmitteeDTO dto, List<MultipartFile> mfList) throws NotFoundException, IOException, NotSupportedException, BadRequestException {

        //project 활성화 여부 확인
        ProjectVO project = projectService.getByName(projectName);
        if(project.getActivated() != 1) throw new NotAcceptableStatusException("project is closed");

        //이미지 갯수, sign 오브젝트 갯수 비교
        if(mfList.size() != dto.getSubmitteeObjectSigns().size())
            throw new BadRequestException("img size and sign objects size should be same");

        //이미지 이름 동일여부 확인
        for(int i = 0; i < mfList.size(); i++){
            MultipartFile imf = mfList.get(i);
            int p = Objects.requireNonNull(imf.getOriginalFilename()).lastIndexOf(".");
            String iFileName = imf.getOriginalFilename().substring(0, p);
            for(int k = i + 1; k < mfList.size(); k++){
                MultipartFile kmf = mfList.get(k);
                int t = Objects.requireNonNull(kmf.getOriginalFilename()).lastIndexOf(".");
                String kFileName = kmf.getOriginalFilename().substring(0, t);
                if(iFileName.equals(kFileName))
                    throw new BadRequestException("file name should be different to each other");
            }
        }

        SubmitteeVO submittee = SubmitteeVO.builder()
                .name(UUID.randomUUID().toString())
                .studentName(dto.getStudentName())
                .studentId(dto.getStudentId())
                .activated(1)
                .project(project)
                .build();

        SubmitteeVO submitteeVO = this.save(submittee);
        SubmitteeDTO submitteeDTO = objectConverter.submitteeVOToDTO(submitteeVO);

        //이미지 파일 이름과 json data(sign object)의 일치 확인, 일치하면 object 저장
        for(SubmitteeObjectSignDTO signDTO : dto.getSubmitteeObjectSigns()){
            boolean isPresent = false;
            for(MultipartFile mf : mfList){
                int i = Objects.requireNonNull(mf.getOriginalFilename()).lastIndexOf(".");
                String fileName = mf.getOriginalFilename().substring(0, i);
                if(fileName.equals(signDTO.getName())){
                    SubmitteeObjectSignVO savedSignVO = submitteeObjectSignService.save(signDTO, mf, submitteeVO);
                    SubmitteeObjectSignDTO convertedSignDTO = objectConverter.submitteeObjectSignVOToDTO(savedSignVO);
                    convertedSignDTO.setSubmitteeObjectSignImg(objectConverter.submitteeObjectSignImgVOToDTO(savedSignVO.getSubmitteeObjectSignImg()));
                    submitteeDTO.getSubmitteeObjectSigns().add(convertedSignDTO);

                    isPresent = true;
                    break;
                }
            }

            if(!isPresent)
                throw new BadRequestException("there's no connection between file and data");
        }

        for(SubmitteeObjectTextDTO textDTO : dto.getSubmitteeObjectTexts()){
            SubmitteeObjectTextVO savedTestDTO = submitteeObjectTextService.save(textDTO, submitteeVO);
            submitteeDTO.getSubmitteeObjectTexts().add(objectConverter.submitteeObjectTextVOToDTO(savedTestDTO));
        }

        for(SubmitteeObjectCheckboxDTO checkboxDTO : dto.getSubmitteeObjectCheckboxes()){
            SubmitteeObjectCheckboxVO savedCheckboxDTO = submitteeObjectCheckboxService.save(checkboxDTO, submitteeVO);
            submitteeDTO.getSubmitteeObjectCheckboxes().add(objectConverter.submitteeObjectCheckboxVOToDTO(savedCheckboxDTO));
        }

        return submitteeDTO;
    }

    @Transactional(readOnly = true)
    public SubmitteeVO getByIdx(Long idx) throws NotFoundException {
        Optional<SubmitteeVO> optional = submitteeRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submittee idx");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public SubmitteeDTO getWithPdfAndObjectsByName(String name) throws AuthException, NotFoundException {

        UserVO user = userService.getMyUserWithAuthorities();
        SubmitteeVO submittee = this.getByName(name);
        SubmitteeDTO submitteeDTO = objectConverter.submitteeVOToDTO(submittee);

        if(submittee.getProject().getUser() != user)
            throw new AuthException("not your own submittee");

        PdfVO pdfVO = submittee.getProject().getPdf();
        submitteeDTO.setPdf(objectConverter.pdfVOToDTO(pdfVO));

        List<SubmitteeObjectVO> submitteeObjects = submittee.getSubmitteeObjects();
        for(SubmitteeObjectVO object : submitteeObjects){
            switch(object.getObjectType().getName()){
                case "OBJECT_TYPE_SIGN":
                    SubmitteeObjectSignVO signEm = submitteeObjectSignService.getByIdx(object.getIdx());
                    submitteeDTO.getSubmitteeObjectSigns().add(objectConverter.submitteeObjectSignVOToDTO(signEm));
                    break;
                case "OBJECT_TYPE_CHECKBOX":
                    SubmitteeObjectCheckboxVO checkboxEm = submitteeObjectCheckboxService.getByIdx(object.getIdx());
                    submitteeDTO.getSubmitteeObjectCheckboxes().add(objectConverter.submitteeObjectCheckboxVOToDTO(checkboxEm));
                    break;
                case "OBJECT_TYPE_TEXT":
                    SubmitteeObjectTextVO textEm = submitteeObjectTextService.getByIdx(object.getIdx());
                    submitteeDTO.getSubmitteeObjectTexts().add(objectConverter.submitteeObjectTextVOToDTO(textEm));
                    break;
            }
        }
        return submitteeDTO;
    }

    private SubmitteeVO getByName(String name) throws NotFoundException {
        Optional<SubmitteeVO> optional = submitteeRepository.findByName(name);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submittee name");

        return optional.get();

    }
}
