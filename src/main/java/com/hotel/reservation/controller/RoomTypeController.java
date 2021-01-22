package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/room-type")
public class RoomTypeController {

    private RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }


    @GetMapping("/")
    public List<RoomType> getRoomTypes() {
        return roomTypeService.getRoomTypes();
    }

    @GetMapping("/{roomTypeId}")
    public RoomType getRoomTypeById(@PathVariable("roomTypeId") Long id) {
        return roomTypeService.getRoomTypeById(id);
    }

    @GetMapping("/{roomTypeId}/rooms")
    public List<Room> getRooms(@PathVariable("roomTypeId") Long id) {
        return roomTypeService.getRoomsByTypeId(id);
    }

    @PostMapping("/")
    public RoomType saveRoom(@RequestBody @Valid RoomType roomType) {
        if (Objects.nonNull(roomType.getId()) && 0L != roomType.getId()) {
            throw new RuntimeException("Room type id must be null or zero");
        }
        return roomTypeService.saveRoomType(roomType);
    }


    @PostMapping("/{roomTypeId}/rooms")
    public Room saveOrder(@PathVariable("roomTypeId") Long id, @RequestBody Room room) {
        return roomTypeService.saveRoomByType(id, room);
    }

    @PutMapping("/{roomTypeId}")
    public RoomType updateRoomType(@PathVariable("roomTypeId") Long id, @RequestBody RoomType roomType) {
        return roomTypeService.updateRoomTypeById(id, roomType);
    }

    @PutMapping("/{roomTypeId}/rooms")
    public Room updateRoom(@PathVariable("roomTypeId") Long id, @RequestBody Room room) {
        return roomTypeService.saveRoomByType(id, room);
    }

    @DeleteMapping("/{roomTypeId}")
    public String deleteRoomTypeById(@PathVariable("roomTypeId") Long id) {
        return roomTypeService.deleteRoomTypeById(id);
    }

    @DeleteMapping("/")
    public void deleteAllRoomTypes() {
        roomTypeService.deleteAllRoomTypes();
    }

    @DeleteMapping("/{roomTypeId}/rooms")
    public void deleteRoomsByRoomTypeId(@PathVariable("roomTypeId") Long id) {
        roomTypeService.deleteRoomsByRoomTypeId(id);
    }

}
