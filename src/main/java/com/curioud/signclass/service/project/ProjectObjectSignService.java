package com.curioud.signclass.service.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
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

        ProjectObjectSignVO vo;
        ObjectTypeVO objectType = objectTypeService.getByName("OBJECT_TYPE_SIGN");

        if(dto.getIdx() == null){

            vo = ProjectObjectSignVO.builder()
                    .xPosition(dto.getXPosition())
                    .yPosition(dto.getYPosition())
                    .width(dto.getWidth())
                    .height(dto.getHeight())
                    .rotate(dto.getRotate())
                    .objectType(objectType)
                    .page(dto.getPage())
                    .project(project)
                    .type(dto.getType())
                    .build();

        }else{
            throw new UnsupportedOperationException("you can't update exist project object");
//            vo = this.getByIdx(dto.getIdx());
//            vo.setXPosition(dto.getXPosition());
//            vo.setYPosition(dto.getYPosition());
//            vo.setWidth(dto.getWidth());
//            vo.setHeight(dto.getHeight());
//            vo.setRotate(dto.getRotate());
//            vo.setPage(dto.getPage());
//            vo.setObjectType(objectType);
//            vo.setType(dto.getType());
////            vo.setProject(); 프로젝트 수정불가
        }

        return this.save(vo);
    }

    @Transactional(readOnly = true)
    public ProjectObjectSignVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectSignVO> optional = projectObjectSignRepository.findById(idx);
        if(optional.isEmpty())
            throw new NotFoundException("invalid sign object idx");
        return optional.get();
    }

}
