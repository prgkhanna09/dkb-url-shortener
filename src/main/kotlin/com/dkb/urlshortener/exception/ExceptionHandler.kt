package com.dkb.urlshortener.handler.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
public class ExceptionHandler {

    @ExceptionHandler(InvalidURLException::class)
    fun handleInvalidURLException() = ResponseEntity("Invalid URL passed as the parameter", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(UrlNotFoundException::class)
    fun handleUrlNotFoundException() = ResponseEntity("URL not found", HttpStatus.NO_CONTENT)

    @ExceptionHandler(UrlPersistException::class)
    fun handleUrlPersistException() = ResponseEntity("Error while persisting the URL", HttpStatus.INTERNAL_SERVER_ERROR)

}

class InvalidURLException : RuntimeException()
class UrlNotFoundException : RuntimeException()
class UrlPersistException : RuntimeException()
