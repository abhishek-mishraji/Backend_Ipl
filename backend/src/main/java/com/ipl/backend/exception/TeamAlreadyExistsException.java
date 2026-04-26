package com.ipl.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeamAlreadyExistsException extends RuntimeException {

    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}