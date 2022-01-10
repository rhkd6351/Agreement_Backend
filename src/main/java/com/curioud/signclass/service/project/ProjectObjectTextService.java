package com.curioud.signclass.service.project;


import com.curioud.signclass.domain.etc.ObjectTypeVO;
import com.curioud.signclass.domain.project.ProjectObjectTextVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.ProjectObjectTextDTO;
import com.curioud.signclass.repository.project.ProjectObjectTextRepository;
import com.curioud.signclass.service.etc.ObjectTypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectObjectTextService {

    @Autowired
    ProjectObjectTextRepository projectObjectTextRepository;

    @Autowired
    ObjectTypeService objectTypeService;

    public ProjectObjectTextVO save(ProjectObjectTextVO vo){
        return projectObjectTextRepository.save(vo);
    }

    public ProjectObjectTextVO save(ProjectObjectTextDTO dto, ProjectVO project) throws NotFoundException {

        ProjectObjectTextVO vo;
        ObjectTypeVO objectType = objectTypeService.getByName("OBJECT_TYPE_TEXT");

        if(dto.getIdx() == null){

            vo = ProjectObjectTextVO.builder()
                    .xPosition(dto.getXPosition())
                    .yPosition(dto.getYPosition())
                    .width(dto.getWidth())
                    .height(dto.getHeight())
                    .rotate(dto.getRotate())
                    .page(dto.getPage())
                    .project(project)
                    .objectType(objectType)
                    .type(dto.getType())
                    .fontSize(dto.getFontSize())
                    .color(dto.getColor())
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
//            vo.setColor(dto.getColor());
//            vo.setFontSize(dto.getFontSize());
////            vo.setProject(); 프로젝트 수정불가
        }

        return this.save(vo);
    }

    public ProjectObjectTextVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectTextVO> optional = projectObjectTextRepository.findById(idx);
        if(optional.isEmpty())
            throw new NotFoundException("invalid text object idx");
        return optional.get();
    }

}
