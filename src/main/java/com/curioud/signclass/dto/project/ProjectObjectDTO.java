package com.curioud.signclass.dto.project;


import com.curioud.signclass.dto.etc.ObjectTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectObjectDTO {

    Long idx;

    int xPosition;

    int yPosition;

    int width;

    int height;

    float rotate;

    int page;

    ProjectDTO project;

    ObjectTypeDTO objectType;
    
}
