package com.hotel.reservation.adapter;

import com.hotel.reservation.entity.RoomType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoomTypeAdapter {

    private final RoomType roomType;

    public RoomTypeAdapter(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomTypeAdapter() {
        this(new RoomType());
    }

    public RoomType toRoomType() {
        return roomType;
    }


    /*
    getters
     */
    public Long getId() {
        return roomType.getId();
    }

    @NotNull(message = "label mustn't be null")
    @NotEmpty(message = "label mustn't be empty")
    public String getLabel() {
        return roomType.getDescription();
    }

    public String getDescription() {
        return roomType.getDescription();
    }


    /*
   setters
    */
    public void setId(Long id){}

    public void setLabel(@NotNull(message = "label mustn't be null")
                         @NotEmpty(message = "label mustn't be empty")
                                 String label) {
        roomType.setDescription(label);
    }

    public void setDescription(String description) {
        roomType.setDescription(description);
    }



}
