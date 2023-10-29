package com.learning.springboot.checklistapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(Exception ex, WebRequest request) throws Exception {
        log.error("An error happened to call API: {}", ex);

        return new ResponseEntity<>(new ExceptionalResponse(LocalDateTime.now(), ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY),
                HttpStatus.UNPROCESSABLE_ENTITY);

    }
}
