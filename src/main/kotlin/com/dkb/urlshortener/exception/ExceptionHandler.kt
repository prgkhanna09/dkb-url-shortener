package com.dkb.urlshortener.handler.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
public class ExceptionHandler {

    @ExceptionHandler(InvalidURLException::class)
    fun handleInvalidURLException() = ResponseEntity("Invalid URL passed as the parameter", HttpStatus.BAD_REQUEST)
}

class InvalidURLException : RuntimeException()
