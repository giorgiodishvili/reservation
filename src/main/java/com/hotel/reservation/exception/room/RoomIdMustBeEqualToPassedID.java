package com.hotel.reservation.exception.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Room id must be equal to passed ID")
public class RoomIdMustBeEqualToPassedID extends RuntimeException{
}
