package com.ipl.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeamDoesNotExistException extends RuntimeException {

    public TeamDoesNotExistException(String message) {
        super(message);
    }
}