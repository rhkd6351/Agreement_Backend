package com.curioud.signclass.repository.submittee;

import com.curioud.signclass.domain.submittee.SubmitteeVO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubmitteeRepository extends JpaRepository<SubmitteeVO, Long> {

}
