package com.curioud.signclass.repository.project;

import com.curioud.signclass.domain.project.ProjectVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectVO, Long> {

    public Optional<ProjectVO> findOneByName(String name);

}
