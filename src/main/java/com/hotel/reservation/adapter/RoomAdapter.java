package com.hotel.reservation.adapter;

import com.hotel.reservation.entity.Room;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoomAdapter {
    private final Room room;

    public RoomAdapter(Room room) {
        this.room = room;
    }

    public RoomAdapter() {
        this(new Room());
    }

    public Room toRoom() {
        return room;
    }

    /*
    getters
     */
    @NotNull(message = "label mustn't be null")
    @NotEmpty(message = "label mustn't be empty")
    public String getLabel() {
        return room.getLabel();
    }

    public String getDescription() {
        return room.getDescription();
    }


    /*
    setters
     */
    public void setLabel(String label) {
        room.setLabel(label);
    }

    public void setDescription(String description) {
        room.setDescription(description);
    }

}
