package com.atcdi.opengis.reponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public StandardResponse globalRuntimeException(Exception e){
        if (e.getCause() != null){
            log.error(e.getCause().toString());
        }
        return StandardResponse.failure(e.getMessage());
    }
}
