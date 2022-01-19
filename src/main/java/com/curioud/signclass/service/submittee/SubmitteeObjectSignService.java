package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.etc.ObjectType;
import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignDTO;
import com.curioud.signclass.repository.submittee.SubmitteeObjectSignRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Optional;


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

        ObjectTypeVO objectType = objectTypeService.getByName(ObjectType.SIGN.getName());
        SubmitteeObjectSignImgVO savedImgVO = submitteeObjectSignImgService.save(mf);

        if(dto.getIdx() == null){

            SubmitteeObjectSignVO vo = SubmitteeObjectSignVO.builder()
                    .name(dto.getName())
                    .disposition(new Disposition(dto.getXPosition(), dto.getYPosition(), dto.getWidth(), dto.getHeight(), dto.getRotate(), dto.getPage()))
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

    @Transactional(readOnly = true)
    public SubmitteeObjectSignVO getByIdx(Long idx) throws NotFoundException {
        Optional<SubmitteeObjectSignVO> optional = submitteeObjectSignRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid submitteeObjectSign idx");

        return optional.get();
    }

}








