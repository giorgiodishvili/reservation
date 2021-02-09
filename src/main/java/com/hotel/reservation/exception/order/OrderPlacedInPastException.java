package com.hotel.reservation.exception.order;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// აქ მგონია რომ BAD_REQUEST სჯობს. და Order Placed in PAST რანაირი კონფლიქტია ?
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Order Placed in PAST")
public class OrderPlacedInPastException extends RuntimeException {
}
