package com.curioud.signclass.service.project;


import com.curioud.signclass.domain.etc.ObjectType;
import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.project.ProjectObjectCheckboxVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.ProjectObjectCheckboxDTO;
import com.curioud.signclass.repository.project.ProjectObjectCheckboxRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectObjectCheckboxService {

    @Autowired
    ProjectObjectCheckboxRepository projectObjectCheckboxRepository;

    @Autowired
    ObjectTypeService objectTypeService;

    @Transactional
    public ProjectObjectCheckboxVO save(ProjectObjectCheckboxVO vo){
        return projectObjectCheckboxRepository.save(vo);
    }

    @Transactional
    public ProjectObjectCheckboxVO save(ProjectObjectCheckboxDTO dto, ProjectVO project) throws NotFoundException {

        ProjectObjectCheckboxVO vo;
        ObjectTypeVO objectType = objectTypeService.getByName(ObjectType.CHECKBOX.getName());

        if(dto.getIdx() == null){

            vo = ProjectObjectCheckboxVO.builder()
                    .name(dto.getName())
                    .disposition(new Disposition(dto.getXPosition(), dto.getYPosition(), dto.getWidth(), dto.getHeight(), dto.getRotate(), dto.getPage()))
                    .project(project)
                    .objectType(objectType)
                    .color(dto.getColor())
                    .type(dto.getType())
                    .build();

        }else{
            throw new UnsupportedOperationException("you can't update exist project object");
        }

        return this.save(vo);
    }

    @Transactional(readOnly = true)
    public ProjectObjectCheckboxVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectCheckboxVO> optional = projectObjectCheckboxRepository.findById(idx);
        if(optional.isEmpty())
            throw new NotFoundException("invalid checkbox object idx");
        return optional.get();
    }

}
