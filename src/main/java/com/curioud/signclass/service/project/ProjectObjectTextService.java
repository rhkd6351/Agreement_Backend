package com.curioud.signclass.service.project;


import com.curioud.signclass.domain.etc.ObjectType;
import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.project.ProjectObjectTextVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.ProjectObjectTextDTO;
import com.curioud.signclass.repository.project.ProjectObjectTextRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectObjectTextService {

    @Autowired
    ProjectObjectTextRepository projectObjectTextRepository;

    @Autowired
    ObjectTypeService objectTypeService;

    @Transactional
    public ProjectObjectTextVO save(ProjectObjectTextVO vo){
        return projectObjectTextRepository.save(vo);
    }

    @Transactional
    public ProjectObjectTextVO save(ProjectObjectTextDTO dto, ProjectVO project) throws NotFoundException {

        ProjectObjectTextVO vo;
        ObjectTypeVO objectType = objectTypeService.getByName(ObjectType.TEXT.getName());

        if(dto.getIdx() == null){

            vo = ProjectObjectTextVO.builder()
                    .name(dto.getName())
                    .disposition(new Disposition(dto.getXPosition(), dto.getYPosition(), dto.getWidth(), dto.getHeight(), dto.getRotate(), dto.getPage()))
                    .project(project)
                    .objectType(objectType)
                    .type(dto.getType())
                    .fontSize(dto.getFontSize())
                    .color(dto.getColor())
                    .build();

        }else{
            throw new UnsupportedOperationException("you can't update exist project object");
        }

        return this.save(vo);
    }

    @Transactional(readOnly = true)
    public ProjectObjectTextVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectTextVO> optional = projectObjectTextRepository.findById(idx);
        if(optional.isEmpty())
            throw new NotFoundException("invalid text object idx");
        return optional.get();
    }

}
