package com.curioud.signclass.repository.project;

import com.curioud.signclass.domain.project.PdfVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdfRepository extends JpaRepository<PdfVO, Long> {

    public Optional<PdfVO> findOneByName(String name);

}
