package com.curioud.signclass.repository.project;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.user.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectVO, Long> {

    public Optional<ProjectVO> findOneByName(String name);

    @EntityGraph(attributePaths = "submittees")
    public Page<ProjectVO> findWithSubmitteesWhereByUserAndActivatedGreaterThan(UserVO user, Pageable pageable, int activated);

    @EntityGraph(attributePaths = {"submittees", "projectObjects", "pdf"})
    public ProjectVO findWithSubmitteesAndProjectObjectsAndPdfByName(String name);

    @EntityGraph(attributePaths = {"projectObjects", "pdf"})
    public ProjectVO findWithProjectObjectsAndPdfByName(String name);

//    @EntityGraph(attributePaths = "projectObjects")
//    public Optional<ProjectVO> findWithProjectObjectsByName(String name);

}
