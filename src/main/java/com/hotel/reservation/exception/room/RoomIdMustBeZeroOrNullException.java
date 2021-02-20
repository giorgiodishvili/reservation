package com.hotel.reservation.exception.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Room id must be null or zero")
public class RoomIdMustBeZeroOrNullException extends RuntimeException {
}
