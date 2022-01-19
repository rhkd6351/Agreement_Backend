package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.etc.ObjectType;
import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.submittee.SubmitteeObjectCheckboxVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectCheckboxDTO;
import com.curioud.signclass.repository.submittee.SubmitteeObjectCheckboxRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Optional;


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

        ObjectTypeVO objectType = objectTypeService.getByName(ObjectType.CHECKBOX.getName());

        if(dto.getIdx() == null){

            SubmitteeObjectCheckboxVO vo = SubmitteeObjectCheckboxVO.builder()
                    .name(dto.getName())
                    .disposition(new Disposition(dto.getXPosition(), dto.getYPosition(), dto.getWidth(), dto.getHeight(), dto.getRotate(), dto.getPage()))
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

    @Transactional(readOnly = true)
    public SubmitteeObjectCheckboxVO getByIdx(Long idx) throws NotFoundException {
        Optional<SubmitteeObjectCheckboxVO> optional = submitteeObjectCheckboxRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submitteeObjectCheckbox idx");

        return optional.get();
    }

}








