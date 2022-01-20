package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.SubmitteePdfVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import com.curioud.signclass.dto.ValidationGroups;
import com.curioud.signclass.dto.project.PdfDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitteeDTO {

    Long idx;

    String name;

    @JsonProperty("student_name")
    @NotBlank(groups = {ValidationGroups.submitteeSubmitGroup.class}, message = "학생 이름이 입력되지 않았습니다.")
    String studentName;

    @JsonProperty("student_id")
    @NotNull(groups = {ValidationGroups.submitteeSubmitGroup.class}, message = "학번이 입력되지 않았습니다.")
    @Range(groups = {ValidationGroups.submitteeSubmitGroup.class}, min = 1,  message = "학번이 입력되지 않았습니다.")
    int studentId;

    int activated;

    @JsonProperty("reg_date")
    LocalDateTime regDate;

    ProjectVO project;

    PdfDTO pdf;

    SubmitteePdfDTO submitteePdfDTO;

    @JsonProperty("submittee_object_checkboxes")
    List<SubmitteeObjectCheckboxDTO> submitteeObjectCheckboxes;

    @JsonProperty("submittee_object_texts")
    List<SubmitteeObjectTextDTO> submitteeObjectTexts;

    @JsonProperty("submittee_object_signs")
    List<SubmitteeObjectSignDTO> submitteeObjectSigns;

    public SubmitteeVO toEntity(ProjectVO project, SubmitteePdfVO pdf){
        return SubmitteeVO.builder()
                .name(this.name == null ? UUID.randomUUID().toString() : name)
                .studentName(studentName)
                .studentId(studentId)
                .activated(1)
                .project(project)
                .submitteePdf(pdf)
                .build();
    }


    
}









