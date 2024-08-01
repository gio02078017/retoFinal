package com.bancolombia.retoFinal.config;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(String errorBody){
        super(errorBody);
    }
}
