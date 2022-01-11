package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.ProjectObjectSignVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignDTO;
import com.curioud.signclass.repository.submittee.SubmitteeObjectSignRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.io.IOException;


@Service
public class SubmitteeObjectSignService {

    SubmitteeObjectSignRepository submitteeObjectSignRepository;

    ObjectTypeService objectTypeService;
    SubmitteeObjectSignImgService submitteeObjectSignImgService;

    public SubmitteeObjectSignService(SubmitteeObjectSignRepository submitteeObjectSignRepository, ObjectTypeService objectTypeService, SubmitteeObjectSignImgService submitteeObjectSignImgService) {
        this.submitteeObjectSignRepository = submitteeObjectSignRepository;
        this.objectTypeService = objectTypeService;
        this.submitteeObjectSignImgService = submitteeObjectSignImgService;
    }

    @Transactional
    public SubmitteeObjectSignVO save(SubmitteeObjectSignVO vo) {
        return submitteeObjectSignRepository.save(vo);
    }

    @Transactional
    public SubmitteeObjectSignVO save(SubmitteeObjectSignDTO dto, MultipartFile mf, SubmitteeVO submitteeVO) throws NotFoundException, IOException, NotSupportedException {

        ObjectTypeVO objectType = objectTypeService.getByName("OBJECT_TYPE_SIGN");
        SubmitteeObjectSignImgVO savedImgVO = submitteeObjectSignImgService.save(mf);

        if(dto.getIdx() == null){

            SubmitteeObjectSignVO vo = SubmitteeObjectSignVO.builder()
                    .name(dto.getName())
                    .xPosition(dto.getXPosition())
                    .yPosition(dto.getYPosition())
                    .width(dto.getWidth())
                    .height(dto.getHeight())
                    .rotate(dto.getRotate())
                    .page(dto.getPage())
                    .submittee(submitteeVO)
                    .objectType(objectType)
                    .type(dto.getType())
                    .submitteeObjectSignImg(savedImgVO)
                    .build();

            return this.save(vo);

        }else{
            throw new UnsupportedOperationException("you can't update submitted project");
        }

    }

}








