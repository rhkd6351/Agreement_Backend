package com.curioud.signclass.service.user;

import com.curioud.signclass.domain.user.AuthVO;
import com.curioud.signclass.repository.user.AuthRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthFindService {

    @Autowired
    AuthRepository authRepository;

    @Transactional(readOnly = true)
    public AuthVO getByName(String name) throws NotFoundException {
        Optional<AuthVO> auth = authRepository.findById(name);

        if(auth.isEmpty())
            throw new NotFoundException("invalid auth name");

        return auth.get();
    }

}
