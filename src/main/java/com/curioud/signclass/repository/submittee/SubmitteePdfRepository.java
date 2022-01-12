package com.curioud.signclass.repository.submittee;

import com.curioud.signclass.domain.submittee.SubmitteePdfVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmitteePdfRepository extends JpaRepository<SubmitteePdfVO, Long> {

    public Optional<SubmitteePdfVO> findOneByName(String name);

}
