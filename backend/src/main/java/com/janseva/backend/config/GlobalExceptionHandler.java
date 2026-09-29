package com.janseva.backend.config;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(Exception.class)
public Map<String, Object> handleException(

        Exception e

) {

    return Map.of(

            "success", false,

            "error",
            e.getMessage(),

            "status",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
}


}
