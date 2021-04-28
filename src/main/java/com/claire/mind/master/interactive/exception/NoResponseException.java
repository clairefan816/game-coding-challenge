package com.claire.mind.master.interactive.exception;

public class NoResponseException extends Exception{
    private String message;
    public NoResponseException(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
