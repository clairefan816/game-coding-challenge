package com.claire.mind.master.interactive.exception;

public class InvalidGuessException extends Exception{
    private String message;
    public InvalidGuessException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
