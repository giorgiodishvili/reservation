package com.hotel.reservation.exception.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    public HttpStatus httpStatus;

    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationException(String msg, HttpStatus forbidden) {
        super(msg);
    }

    public JwtAuthenticationException() {
        super("Jwt Token not valid");
    }

    public JwtAuthenticationException(String s) {
        super(s);
    }
}
