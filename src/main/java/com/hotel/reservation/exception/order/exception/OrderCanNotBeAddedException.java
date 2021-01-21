package com.hotel.reservation.exception.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Time is not Available, Order Can not be added")
public class OrderCanNotBeAddedException extends RuntimeException {
}
