package com.curioud.signclass.service.user;


import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.repository.user.UserRepository;
import com.curioud.signclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.Optional;

@Service
public class UserFindService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserVO getMyUserWithAuthorities() throws AuthException {
        Optional<UserVO> userVO = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneById);

        if(userVO.isEmpty())
            throw new AuthException("invalid user or token");

        return userVO.get();
    }
}
