package com.curioud.signclass.dto.submittee;


import com.curioud.signclass.domain.project.ProjectVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    
}
