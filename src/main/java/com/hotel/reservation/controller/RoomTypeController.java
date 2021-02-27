package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.RoomAdapter;
import com.hotel.reservation.adapter.RoomTypeAdapter;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.service.RoomService;
import com.hotel.reservation.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/room-type")
@Validated
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomService roomService;


    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService, RoomService roomService) {
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
    }


    @GetMapping
    public Page<RoomType> getRoomTypes(Pageable pageable) {
        return roomTypeService.getAllRoomTypes(pageable);
    }

    @GetMapping("/{roomTypeId}")
    public RoomType getRoomTypeById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId) {
        return roomTypeService.getRoomTypeById(roomTypeId);
    }

    @GetMapping("/{roomTypeId}/rooms")
    public Page<Room> getRoomsByRoomTypeId(@PathVariable("roomTypeId") @Min(1) Long roomTypeId,Pageable pageable) {
        return roomTypeService.getAllRoomsByRoomTypeId(roomTypeId,pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomTypeAdapter createRoomType(@RequestBody @Valid RoomTypeAdapter roomTypeAdapter) {
        return roomTypeService.createRoomType(roomTypeAdapter);
    }


    @PostMapping("/{roomTypeId}/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public Room createRoomByRoomType(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @RequestBody @Valid RoomAdapter roomAdapter) {
        return roomService.createRoomByRoomType(roomTypeId, roomAdapter);
    }

    @PutMapping("/{roomTypeId}/rooms/{roomId}")
    public Room updateRoom(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @PathVariable("roomId") @Min(1) Long roomId, @RequestBody @Valid RoomAdapter roomAdapter) {
        return roomService.updateRoomByRoomTypeAndRoomId(roomTypeId, roomId, roomAdapter);
    }

    @DeleteMapping("/{roomTypeId}/rooms/{roomId}")
    public Room deleteRoomById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @PathVariable("roomId") @Min(1) Long roomId) {
        return roomService.deleteRoomById(roomTypeId, roomId);
    }

    @PutMapping("/{roomTypeId}")
    public RoomType updateRoomTypeById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @RequestBody @Valid RoomTypeAdapter roomTypeAdapter) {
        return roomTypeService.updateRoomTypeById(roomTypeId, roomTypeAdapter);
    }

    @DeleteMapping("/{roomTypeId}")
    public RoomType deleteRoomTypeById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId) {
        return roomTypeService.deleteRoomTypeById(roomTypeId);
    }

}
