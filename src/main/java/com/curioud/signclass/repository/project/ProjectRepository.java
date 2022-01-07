package com.curioud.signclass.repository.project;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.user.UserVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectVO, Long> {

    public Optional<ProjectVO> findOneByName(String name);

    @EntityGraph(attributePaths = "submittees")
    public List<ProjectVO> findWithSubmitteesByUser(UserVO user);

}
