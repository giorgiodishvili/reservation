package com.hotel.reservation.exception.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// აქ მგონია რომ BAD_REQUEST სჯობს.
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Order id must be null or zero")
public class OrderIdMustBeZeroOrNullException extends RuntimeException {
}
