package com.hotel.reservation.exception.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order Not Found")
public class OrderNotFoundException extends RuntimeException {
}