package com.curioud.signclass.exception;


import com.curioud.signclass.dto.etc.MessageDTO;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.transaction.NotSupportedException;
import java.nio.file.NoSuchFileException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageDTO> NotFoundException(NotFoundException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<MessageDTO> NoSuchFileException(NoSuchFileException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<MessageDTO> IllegalAccessException(IllegalAccessException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<MessageDTO> DuplicateMemberException(DuplicateMemberException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity<MessageDTO> NotSupportedException(NotSupportedException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageDTO> BadRequestException(BadRequestException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<MessageDTO> NotAcceptableStatusException(NotAcceptableStatusException e){
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> Exception(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }





}

