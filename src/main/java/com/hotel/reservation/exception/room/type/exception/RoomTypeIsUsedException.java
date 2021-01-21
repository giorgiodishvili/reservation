package com.hotel.reservation.exception.room.type.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Room Type is Being USED")
public class RoomTypeIsUsedException extends RuntimeException {

}
