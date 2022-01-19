package com.curioud.signclass.service.project;


import com.curioud.signclass.domain.etc.ObjectType;
import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.Disposition;
import com.curioud.signclass.domain.project.ProjectObjectSignVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.ProjectObjectSignDTO;
import com.curioud.signclass.repository.project.ProjectObjectSignRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectObjectSignService {

    @Autowired
    ProjectObjectSignRepository projectObjectSignRepository;

    @Autowired
    ObjectTypeService objectTypeService;

    @Transactional
    public ProjectObjectSignVO save(ProjectObjectSignVO vo){
        return projectObjectSignRepository.save(vo);
    }

    @Transactional
    public ProjectObjectSignVO save(ProjectObjectSignDTO dto, ProjectVO project) throws NotFoundException {

        ObjectTypeVO objectType = objectTypeService.getByName(ObjectType.SIGN.getName());

        if(dto.getIdx() == null){
            ProjectObjectSignVO vo = ProjectObjectSignVO.builder()
                    .name(dto.getName())
                    .disposition(new Disposition(dto.getXPosition(), dto.getYPosition(), dto.getWidth(), dto.getHeight(), dto.getRotate(), dto.getPage()))
                    .objectType(objectType)
                    .project(project)
                    .type(dto.getType())
                    .build();

            return this.save(vo);

        }else{
            throw new UnsupportedOperationException("you can't update project");
        }
    }

    @Transactional(readOnly = true)
    public ProjectObjectSignVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectSignVO> optional = projectObjectSignRepository.findById(idx);
        if(optional.isEmpty())
            throw new NotFoundException("invalid sign object idx");
        return optional.get();
    }

}
