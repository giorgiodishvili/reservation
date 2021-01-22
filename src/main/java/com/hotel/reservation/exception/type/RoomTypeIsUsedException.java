package com.hotel.reservation.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.LOCKED, reason = "Room Type is Being USED")
public class RoomTypeIsUsedException extends RuntimeException {

}
