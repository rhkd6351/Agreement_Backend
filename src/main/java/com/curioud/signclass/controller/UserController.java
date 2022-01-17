package com.curioud.signclass.controller;

import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.ValidationGroups;
import com.curioud.signclass.dto.user.UserDTO;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ObjectConverter objectConverter;

    /** Sign Up
     *
     * @param userDTO 유저 id, password, name
     * @return 가입된 유저 정보
     * @throws DuplicateMemberException 중복된 유저 id
     * @throws NotFoundException 유효하지 않은 권한명
     */
    @PostMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getSignUp(@RequestBody @Validated(ValidationGroups.userSignUpGroup.class) UserDTO userDTO)
            throws DuplicateMemberException, NotFoundException {

        UserVO registeredUserVO = userService.signUp(userDTO);
        UserDTO convertedUserDTO = objectConverter.userVOToDTO(registeredUserVO);

        return new ResponseEntity<>(convertedUserDTO, HttpStatus.OK);
    }

    /** Get User
     *
     * @return 유저정보
     * @throws AuthException 유효하지 않은 토큰
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> getUser() throws AuthException {

        UserVO user = userService.getMyUserWithAuthorities();
        UserDTO userDTO = objectConverter.userVOToDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
