package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("student_name")
    String studentName;

    @JsonProperty("student_id")
    int studentId;

    int activated;

    @JsonProperty("reg_date")
    LocalDateTime regDate;

    ProjectVO project;

    PdfDTO pdf;

    @JsonProperty("submittee_object_checkboxes")
    List<SubmitteeObjectCheckboxDTO> submitteeObjectCheckboxes;

    @JsonProperty("submittee_object_texts")
    List<SubmitteeObjectTextDTO> submitteeObjectTexts;

    @JsonProperty("submittee_object_signs")
    List<SubmitteeObjectSignDTO> submitteeObjectSigns;
    
}
