package com.hotel.reservation.exception.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// აქ მგონია რომ BAD_REQUEST სჯობს.
// @see https://httpstatuses.com/
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Room Label Already Exists")
public class RoomLabelAlreadyExistsException extends RuntimeException {

}
