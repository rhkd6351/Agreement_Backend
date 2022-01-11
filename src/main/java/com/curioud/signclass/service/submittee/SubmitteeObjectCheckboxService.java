package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectCheckboxVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectCheckboxDTO;
import com.curioud.signclass.repository.submittee.SubmitteeObjectCheckboxRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.io.IOException;


@Service
public class SubmitteeObjectCheckboxService {

    SubmitteeObjectCheckboxRepository submitteeObjectCheckboxRepository;

    ObjectTypeService objectTypeService;

    public SubmitteeObjectCheckboxService(SubmitteeObjectCheckboxRepository submitteeObjectCheckboxRepository, ObjectTypeService objectTypeService) {
        this.submitteeObjectCheckboxRepository = submitteeObjectCheckboxRepository;
        this.objectTypeService = objectTypeService;
    }

    @Transactional
    public SubmitteeObjectCheckboxVO save(SubmitteeObjectCheckboxVO vo) {
        return submitteeObjectCheckboxRepository.save(vo);
    }

    @Transactional
    public SubmitteeObjectCheckboxVO save(SubmitteeObjectCheckboxDTO dto , SubmitteeVO submitteeVO) throws NotFoundException, IOException, NotSupportedException {

        ObjectTypeVO objectType = objectTypeService.getByName("OBJECT_TYPE_CHECKBOX");

        if(dto.getIdx() == null){

            SubmitteeObjectCheckboxVO vo = SubmitteeObjectCheckboxVO.builder()
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
                    .checked(dto.isChecked())
                    .color(dto.getColor())
                    .build();

            return this.save(vo);

        }else{
            throw new UnsupportedOperationException("you can't update submitted project");
        }

    }

}








