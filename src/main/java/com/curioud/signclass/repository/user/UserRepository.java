package com.curioud.signclass.repository.user;

import com.curioud.signclass.domain.user.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Long> {

    Optional<UserVO> findOneById(String id);

}
