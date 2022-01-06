package com.curioud.signclass.service.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.ProjectObjectCheckboxVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.ProjectObjectCheckboxDTO;
import com.curioud.signclass.repository.project.ProjectObjectCheckboxRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectObjectCheckboxService {

    @Autowired
    ProjectObjectCheckboxRepository projectObjectCheckboxRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ObjectTypeService objectTypeService;

    public ProjectObjectCheckboxVO save(ProjectObjectCheckboxVO vo){
        return projectObjectCheckboxRepository.save(vo);
    }

    public ProjectObjectCheckboxVO save(ProjectObjectCheckboxDTO dto) throws NotFoundException {

        ProjectObjectCheckboxVO vo;
        ObjectTypeVO objectType = objectTypeService.getByName("OBJECT_TYPE_SIGN");

        if(dto.getIdx() == null){
            ProjectVO project = projectService.getByName(dto.getProject().getName());

            vo = ProjectObjectCheckboxVO.builder()
                    .xPosition(dto.getXPosition())
                    .yPosition(dto.getYPosition())
                    .width(dto.getWidth())
                    .height(dto.getHeight())
                    .rotate(dto.getRotate())
                    .page(dto.getPage())
                    .project(project)
                    .objectType(objectType)
                    .color(dto.getColor())
                    .type(dto.getType())
                    .build();

        }else{
            vo = this.getByIdx(dto.getIdx());
            vo.setXPosition(dto.getXPosition());
            vo.setYPosition(dto.getYPosition());
            vo.setWidth(dto.getWidth());
            vo.setHeight(dto.getHeight());
            vo.setRotate(dto.getRotate());
            vo.setPage(dto.getPage());
            vo.setObjectType(objectType);
            vo.setColor(dto.getColor());
            vo.setType(dto.getType());
//            vo.setProject(); 프로젝트 수정불가
        }

        return this.save(vo);
    }

    public ProjectObjectCheckboxVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectCheckboxVO> optional = projectObjectCheckboxRepository.findById(idx);
        if(optional.isEmpty())
            throw new NotFoundException("invalid checkbox object idx");
        return optional.get();
    }

}
