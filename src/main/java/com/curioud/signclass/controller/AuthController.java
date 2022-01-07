package com.curioud.signclass.controller;

import com.curioud.signclass.dto.etc.MessageDTO;
import com.curioud.signclass.dto.user.TokenDTO;
import com.curioud.signclass.dto.user.UserDTO;
import com.curioud.signclass.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    /**
     *
     * @param userDTO id, password 값을 입력받습니다.
     * @return TokenDTO 생성된 jwt를 리턴합니다.
     */
    @PostMapping("/user/login")
    public ResponseEntity<TokenDTO> authorize(@RequestBody UserDTO userDTO) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getId(), userDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        return new ResponseEntity<>(new TokenDTO(jwt), HttpStatus.OK);
    }

    /**
     *
     * @return MessageDTO 토큰값이 유효한지 확인하여 결과값을 리턴합니다.
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageDTO> validCheck(){
        return ResponseEntity.ok(MessageDTO.builder().message("valid token").build());
    }
}