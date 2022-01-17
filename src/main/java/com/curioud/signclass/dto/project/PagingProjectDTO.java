package com.curioud.signclass.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagingProjectDTO {

    List<ProjectDTO> projects = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;

}
