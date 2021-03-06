package com.hotel.reservation.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid Email/Password combination")
public class InvalidEmailOrPasswordException extends RuntimeException {
}
