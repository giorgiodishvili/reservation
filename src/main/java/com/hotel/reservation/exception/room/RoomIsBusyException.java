package com.hotel.reservation.exception.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED, reason = "Room is Busy")
public class RoomIsBusyException extends RuntimeException {
}
