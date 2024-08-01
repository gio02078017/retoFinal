package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.config.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import com.bancolombia.retoFinal.config.Exception400Exception;


@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(Exception400Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleException400Exception(Exception400Exception exception){
        return Mono.just(ResponseEntity.badRequest().body(exception.getMessage()));
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleClientNotFoundException(ClientNotFoundException exception){
        return Mono.just(ResponseEntity.badRequest().body(exception.getMessage()));
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleValidationExceptions(WebExchangeBindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
