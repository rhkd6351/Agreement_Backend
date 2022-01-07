package com.curioud.signclass.exception;


import com.curioud.signclass.dto.etc.MessageDTO;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.transaction.NotSupportedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageDTO> NotFoundException(NotFoundException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<MessageDTO> DuplicateMemberException(DuplicateMemberException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity<MessageDTO> NotSupportedException(NotSupportedException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> Exception(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }



}

