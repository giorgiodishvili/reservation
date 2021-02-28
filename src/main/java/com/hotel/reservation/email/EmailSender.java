package com.hotel.reservation.email;

public interface EmailSender {
    void send(String to, String email);
}
