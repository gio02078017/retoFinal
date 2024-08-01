package com.bancolombia.retoFinal.config;

public class Exception400Exception extends RuntimeException{
    public Exception400Exception(String errorBody) {
        super(errorBody);
    }
}
