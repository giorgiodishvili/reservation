package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping(value = "/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public Iterable<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable("roomId") Long id) {
        return roomService.getRoomById(id);
    }

    @GetMapping("/{roomId}/orders")
    public List<Orders> getOrders(@PathVariable("roomId") Long id) {
        return roomService.getOrdersByRoomId(id);
    }

    @PostMapping("/")
    public Room saveRoom(@RequestBody @Valid Room room) {
        if (Objects.nonNull(room.getId()) && 0L != room.getId()) {
            throw new RuntimeException("Room id must be null or zero");
        }
        return roomService.saveRoom(room);
    }


    @PostMapping("/{roomId}/orders")
    public Orders saveOrder(@PathVariable("roomId") Long id, @RequestBody Orders orders) {
        return roomService.saveOrder(id, orders);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable("roomId") Long id, @RequestBody Room room) {
        return roomService.updateRoomById(id, room);
    }

    @DeleteMapping("/{roomId}")
    public String deleteRoomById(@PathVariable("roomId") Long id) {
        return roomService.deleteRoomById(id);
    }

    @DeleteMapping("/")
    public void deleteRoomById() {
        roomService.deleteAllRooms();
    }

    @DeleteMapping("/{roomId}/orders")
    public void deleteOrderByRoom(@PathVariable("roomId") Long id) {
        roomService.deleteOrdersByRoomId(id);
    }

}
