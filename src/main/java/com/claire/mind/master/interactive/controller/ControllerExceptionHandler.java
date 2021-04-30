package com.claire.mind.master.interactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadRequestException.class)
    public void handleBadRequest(Exception ex) {
        log.error("An error occurred processing request" + ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(IllegalStateException.class)
    public void handleInternalError(Exception ex) {
        log.error("An error occurred processing request" + ex);
    }
}
