package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/room-type")
@Slf4j
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }


    @GetMapping
    public Iterable<RoomType> getRoomTypes() {
        log.trace("in getRoomTypes controller method");// info ნამეტანია. debug-ს კიდე გაგიგებდი.
        return roomTypeService.getAllRoomTypes();
    }

    @GetMapping("/{roomTypeId}")
    public RoomType getRoomTypeById(@PathVariable("roomTypeId") Long roomTypeId) {
        log.trace("in getRoomTypeById controller method");
        return roomTypeService.getRoomTypeById(roomTypeId);
    }

    @GetMapping("/{roomTypeId}/rooms")
    public List<Room> getRoomsByRoomTypeId(@PathVariable("roomTypeId") Long roomTypeId) {
        log.trace("in getRoomTypeById controller method");
        return roomTypeService.getAllRoomsByRoomTypeId(roomTypeId);
    }

    @PostMapping
    public RoomType createRoomType(@RequestBody @Valid RoomType roomType) {
        log.trace("in createRoomType controller method");
        return roomTypeService.createRoomType(roomType);

    }


    @PostMapping("/{roomTypeId}/rooms")
    public Room createRoomByRoomType(@PathVariable("roomTypeId") Long roomTypeId, @RequestBody @Valid Room room) {
        log.trace("in createRoomByRoomType controller method");
        return roomTypeService.createRoomByRoomType(roomTypeId, room);
    }

    @PutMapping("/{roomTypeId}")
    public RoomType updateRoomTypeById(@PathVariable("roomTypeId") Long roomTypeId, @RequestBody @Valid RoomType roomType) {
        log.trace("in updateRoomTypeById controller method");
        return roomTypeService.updateRoomTypeById(roomTypeId, roomType);
    }

    @DeleteMapping("/{roomTypeId}")
    public RoomType deleteRoomTypeById(@PathVariable("roomTypeId") Long roomTypeId) {
        log.trace("in deleteRoomTypeById controller method");
        return roomTypeService.deleteRoomTypeById(roomTypeId);
    }

}
