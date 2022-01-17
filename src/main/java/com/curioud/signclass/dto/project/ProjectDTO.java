package com.curioud.signclass.dto.project;

import com.curioud.signclass.dto.ValidationGroups;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import com.curioud.signclass.dto.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @JsonProperty("reg_date")
    LocalDateTime regDate;

    @JsonProperty("end_date")
    LocalDateTime endDate;

    @JsonProperty("up_date")
    LocalDateTime upDate;

    @JsonProperty("state")
    int activated;

    PdfDTO pdf;

    UserDTO user;

    @JsonProperty("project_objects")
    List<ProjectObjectDTO> projectObjects = new ArrayList<>();

    @JsonProperty("project_object_checkboxes")
    List<ProjectObjectCheckboxDTO> projectObjectCheckboxes = new ArrayList<>();

    @JsonProperty("project_object_texts")
    List<ProjectObjectTextDTO> projectObjectTexts = new ArrayList<>();

    @JsonProperty("project_object_signs")
    List<ProjectObjectSignDTO> projectObjectSigns = new ArrayList<>();

    List<SubmitteeDTO> submittees = new ArrayList<>();

    @JsonProperty("submittee_count")
    int submitteeCount;

}
