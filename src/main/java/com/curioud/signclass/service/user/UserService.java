package com.curioud.signclass.service.user;


import com.curioud.signclass.domain.user.AuthType;
import com.curioud.signclass.domain.user.AuthVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.user.UserDTO;
import com.curioud.signclass.repository.user.AuthRepository;
import com.curioud.signclass.repository.user.UserRepository;
import com.curioud.signclass.util.SecurityUtil;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserVO signUp(UserDTO userDto) throws DuplicateMemberException, NotFoundException {
        if (userRepository.findOneById(userDto.getId()).orElse(null) != null)
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");

        Optional<AuthVO> authOptional = authRepository.findById(AuthType.USER.getName());
        if(authOptional.isEmpty())
            throw new NotFoundException("Invalid auth name");

        AuthVO auth = authOptional.get();

        UserVO user = UserVO.builder()
                .id(userDto.getId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .auth(auth)
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserVO getMyUserWithAuthorities() throws AuthException {
        Optional<UserVO> userVO = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneById);
        if(userVO.isEmpty())
            throw new AuthException("Invalid Token");

        return userVO.get();
    }
}
