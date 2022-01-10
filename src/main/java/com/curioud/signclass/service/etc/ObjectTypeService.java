package com.curioud.signclass.service.etc;

import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.repository.etc.ObjectTypeRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ObjectTypeService {

    @Autowired
    ObjectTypeRepository objectTypeRepository;


    @Transactional(readOnly = true)
    public ObjectTypeVO getByName(String name) throws NotFoundException {
        Optional<ObjectTypeVO> optional = objectTypeRepository.findById(name);

        if(optional.isEmpty())
            throw new NotFoundException("invalid objectType Name");

        return optional.get();
    }

}
