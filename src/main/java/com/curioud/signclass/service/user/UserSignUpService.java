package com.curioud.signclass.service.user;


import com.curioud.signclass.domain.user.AuthType;
import com.curioud.signclass.domain.user.AuthVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.user.UserDTO;
import com.curioud.signclass.repository.user.UserRepository;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSignUpService {

    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    AuthFindService authFindService;

    public UserSignUpService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthFindService authFindService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authFindService = authFindService;
    }

    @Transactional
    public UserDTO signUp(UserDTO userDto) throws DuplicateMemberException, NotFoundException {

        if (userRepository.existsById(userDto.getId()))
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");

        AuthVO auth = authFindService.getByName(AuthType.USER.getName());
        UserVO user = userDto.toEntity(passwordEncoder.encode(userDto.getPassword()), auth);

        return userRepository.save(user).dto();
    }
}
