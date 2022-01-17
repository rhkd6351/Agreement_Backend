package com.curioud.signclass.repository.submittee;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.submittee.SubmitteeVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SubmitteeRepository extends JpaRepository<SubmitteeVO, Long> {

    public Optional<SubmitteeVO> findByName(String name);

    public Page<SubmitteeVO> getByProject(ProjectVO project, Pageable pageable);

}
