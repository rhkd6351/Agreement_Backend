package com.curioud.signclass.repostory;

import com.curioud.signclass.domain.user.AuthVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.repository.user.AuthRepository;
import com.curioud.signclass.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthRepository authRepository;

    @DisplayName("User Create")
    @Test
    void saveUserTest() {
        //given

        Optional<AuthVO> authVO = authRepository.findById("ROLE_USER");
        if(authVO.isEmpty())
            Assertions.fail();

        UserVO userVO = UserVO.builder()
                .id("testId##")
                .password("testPassword##")
                .name("test name")
                .activated(true)
                .auth(authVO.get())
                .build();
        //when
        userRepository.save(userVO);

        //then
        Assertions.assertNotNull(userVO.getIdx());
    }
}
