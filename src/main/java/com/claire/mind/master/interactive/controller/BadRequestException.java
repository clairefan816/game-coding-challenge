package com.claire.mind.master.interactive.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad Request") //400
public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super();
    }
    public BadRequestException(String message){
        super(message);
    }
    public BadRequestException(String message, Throwable cause){
        super(message, cause);
    }
    public BadRequestException(Throwable cause){
        super(cause);
    }
}