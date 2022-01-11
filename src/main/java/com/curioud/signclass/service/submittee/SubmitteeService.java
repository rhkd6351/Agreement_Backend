package com.curioud.signclass.service.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectCheckboxVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectTextVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectCheckboxDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignDTO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectTextDTO;
import com.curioud.signclass.exception.BadRequestException;
import com.curioud.signclass.repository.submittee.SubmitteeRepository;
import com.curioud.signclass.service.project.ProjectService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubmitteeService {

    SubmitteeRepository submitteeRepository;

    SubmitteeObjectSignService submitteeObjectSignService;
    SubmitteeObjectTextService submitteeObjectTextService;
    SubmitteeObjectCheckboxService submitteeObjectCheckboxService;
    ProjectService projectService;
    ObjectConverter objectConverter;

    public SubmitteeService(SubmitteeRepository submitteeRepository, SubmitteeObjectSignService submitteeObjectSignService, SubmitteeObjectTextService submitteeObjectTextService, SubmitteeObjectCheckboxService submitteeObjectCheckboxService, ProjectService projectService, ObjectConverter objectConverter) {
        this.submitteeRepository = submitteeRepository;
        this.submitteeObjectSignService = submitteeObjectSignService;
        this.submitteeObjectTextService = submitteeObjectTextService;
        this.submitteeObjectCheckboxService = submitteeObjectCheckboxService;
        this.projectService = projectService;
        this.objectConverter = objectConverter;
    }

    @Transactional
    public SubmitteeVO save(SubmitteeVO vo){
        return submitteeRepository.save(vo);
    }

    @Transactional
    public SubmitteeDTO saveWithObjects(String projectName, SubmitteeDTO dto, List<MultipartFile> mfList) throws NotFoundException, IOException, NotSupportedException, BadRequestException {

        ProjectVO project = projectService.getByName(projectName);
        if(project.getActivated() != 1) throw new NotAcceptableStatusException("project is closed");


        SubmitteeVO submittee = SubmitteeVO.builder()
                .name(dto.getName())
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

    @Transactional
    public SubmitteeVO getByIdx(Long idx) throws NotFoundException {
        Optional<SubmitteeVO> optional = submitteeRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submittee idx");

        return optional.get();
    }





}
