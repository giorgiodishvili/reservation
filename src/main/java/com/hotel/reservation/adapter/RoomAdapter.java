package com.hotel.reservation.adapter;

import com.hotel.reservation.entity.Room;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoomAdapter {
    private final Room room;
    public long roomTypeId;

    public RoomAdapter(Room room) {
        this.room = room;
    }

    public RoomAdapter() {
        this(new Room());
    }

    public Long getId() {
        return room.getId();
    }

    @NotNull(message = "label mustn't be null")
    @NotEmpty(message = "label mustn't be empty")
    public String getLabel() {
        return room.getLabel();
    }

    public String getDescription() {
        return room.getDescription();
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }
}
