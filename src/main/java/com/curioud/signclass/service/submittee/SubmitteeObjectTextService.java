package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectCheckboxVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectTextVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectTextDTO;
import com.curioud.signclass.repository.submittee.SubmitteeObjectTextRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Optional;


@Service
public class SubmitteeObjectTextService {

    SubmitteeObjectTextRepository submitteeObjectTextRepository;

    ObjectTypeService objectTypeService;

    public SubmitteeObjectTextService(SubmitteeObjectTextRepository submitteeObjectTextRepository, ObjectTypeService objectTypeService) {
        this.submitteeObjectTextRepository = submitteeObjectTextRepository;
        this.objectTypeService = objectTypeService;
    }

    @Transactional
    public SubmitteeObjectTextVO save(SubmitteeObjectTextVO vo) {
        return submitteeObjectTextRepository.save(vo);
    }

    @Transactional
    public SubmitteeObjectTextVO save(SubmitteeObjectTextDTO dto , SubmitteeVO submitteeVO) throws NotFoundException, IOException, NotSupportedException {

        ObjectTypeVO objectType = objectTypeService.getByName("OBJECT_TYPE_TEXT");

        if(dto.getIdx() == null){

            SubmitteeObjectTextVO vo = SubmitteeObjectTextVO.builder()
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
                    .content(dto.getContent())
                    .fontSize(dto.getFontSize())
                    .color(dto.getColor())
                    .build();

            return this.save(vo);

        }else{
            throw new UnsupportedOperationException("you can't update submitted project");
        }

    }

    @Transactional(readOnly = true)
    public SubmitteeObjectTextVO getByIdx(Long idx) throws NotFoundException {
        Optional<SubmitteeObjectTextVO> optional = submitteeObjectTextRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submitteeObjectText idx");

        return optional.get();
    }

}








