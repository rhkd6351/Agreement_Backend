package com.curioud.signclass.controller;

import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.user.UserDTO;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ObjectConverter objectConverter;

    /**
     *
     * @param userDTO
     * id 아이디
     * password 비밀번호
     * name 유저이름
     *
     * @return 가입한 User 정보
     * @throws DuplicateMemberException 이미 존재하는 ID
     * @throws NotFoundException 유효하지 않은 유저 권한
     */
    @PostMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getSignUp(@Valid @RequestBody UserDTO userDTO) throws DuplicateMemberException, NotFoundException {

        UserVO registeredUserVO = userService.signUp(userDTO);
        UserDTO convertedUserDTO = objectConverter.userVOToDTO(registeredUserVO);

        return new ResponseEntity<>(convertedUserDTO, HttpStatus.OK);
    }
}
