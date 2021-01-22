package com.hotel.reservation.exception.order.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Room Placed in PAST")
public class OrderPlacedInPastException extends RuntimeException {
}
