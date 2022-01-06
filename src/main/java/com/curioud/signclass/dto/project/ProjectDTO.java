package com.curioud.signclass.dto.project;

import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    Long idx;

    String name;

    String title;

    String description;

    LocalDateTime regDate;

    LocalDateTime endDate;

    LocalDateTime upDate;

    boolean activated;

    PdfDTO pdf;

    UserDTO user;

    List<ProjectObjectCheckboxDTO> projectObjectCheckboxes;

    List<ProjectObjectTextDTO> projectObjectTexts;

    List<ProjectObjectSignDTO> projectObjectSigns;

    List<SubmitteeDTO> submittees;

}
