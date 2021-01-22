package com.hotel.reservation.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Room Not Found")
public class RoomTypeNotFoundException extends RuntimeException {
}
