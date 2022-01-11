package com.curioud.signclass.repository.submittee;

import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmitteeObjectSignImgRepository extends JpaRepository<SubmitteeObjectSignImgVO, Long> {

    public Optional<SubmitteeObjectSignImgVO> findByName(String name);


}
