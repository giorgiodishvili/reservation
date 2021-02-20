package com.hotel.reservation.exception.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order id must be null or zero")
public class OrderIdMustBeZeroOrNullException extends RuntimeException {
}
