package com.hotel.reservation.exception.order;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Order Placed in PAST")
public class OrderPlacedInPastException extends RuntimeException {
}
