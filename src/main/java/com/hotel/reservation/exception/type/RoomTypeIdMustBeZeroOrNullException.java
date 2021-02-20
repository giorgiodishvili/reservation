package com.hotel.reservation.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Room type id must be null or zero")
public class RoomTypeIdMustBeZeroOrNullException extends RuntimeException {
}
