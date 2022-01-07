package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeDTO {

    Long idx;

    String name;

    LocalDateTime regDate;

    ProjectVO project;

    List<SubmitteeObjectCheckboxDTO> submitteeObjectCheckboxes;

    List<SubmitteeObjectTextDTO> submitteeObjectTexts;

    List<SubmitteeObjectSignDTO> submitteeObjectSigns;
    
}
