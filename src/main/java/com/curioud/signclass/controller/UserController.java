package com.curioud.signclass.controller;

import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.dto.MessageDTO;
import com.curioud.signclass.dto.UserDTO;
import com.curioud.signclass.service.user.UserService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
//    ObjectConverter objectConverter;

    @PostMapping("/user")
    public ResponseEntity<UserDTO> getSignUp(@RequestBody UserDTO userDTO) throws DuplicateMemberException, NotFoundException {

        UserVO registeredUserVO = userService.signUp(userDTO);
        UserDTO convertedUserDTO = ObjectConverter.UserVOToDTO(registeredUserVO);

        List<ProjectVO> projects = registeredUserVO.getProjects();
        registeredUserVO.setPassword("12323");

        registeredUserVO.getProjects().get(1).getProjectObjectSigns();

        return new ResponseEntity<>(convertedUserDTO, HttpStatus.OK);
    }

}
