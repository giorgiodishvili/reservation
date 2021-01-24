package com.hotel.reservation.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Room Type Label Already Exists")
public class RoomTypeLabelAlreadyExistsException extends RuntimeException {

}
