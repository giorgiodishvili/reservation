package com.hotel.reservation.exception.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// NOT_FOUND ხომ არ ჯობია აქ?
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Room Id not found")
public class RoomIdNotFoundException extends RuntimeException {
}
