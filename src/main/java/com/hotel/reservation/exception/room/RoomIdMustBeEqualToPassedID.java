package com.hotel.reservation.exception.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// hmm? აი ეს სად გაქვს გამოყენებული მანახე პლს.
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Room id must be equal to passed ID")
public class RoomIdMustBeEqualToPassedID extends RuntimeException {
}
