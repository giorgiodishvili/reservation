package com.hotel.reservation.exception.room.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Room Label Already Exists")
public class RoomLabelAlreadyExistsException extends RuntimeException {

}
