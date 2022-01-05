package com.curioud.signclass.repository.user;

import com.curioud.signclass.domain.user.AuthVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthVO, String> {

}
