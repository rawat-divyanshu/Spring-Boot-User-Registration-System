package com.rawatdivyanshu.userregistrationsystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserAuthException extends RuntimeException {

    public UserAuthException(String message) {
        super(message);
    }

}
