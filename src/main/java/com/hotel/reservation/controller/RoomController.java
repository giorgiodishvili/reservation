package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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

    @PostMapping
    public Room saveRoom(@RequestBody @Valid Room room) {
        return roomService.createRoom(room);
    }


    @PostMapping("/{roomId}/orders")
    public Orders saveOrder(@PathVariable("roomId") Long id, @RequestBody @Valid Orders orders) {
        return roomService.createOrder(id, orders);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable("roomId") Long id, @RequestBody @Valid Room room) {
        return roomService.updateRoomById(id, room);
    }

    @DeleteMapping("/{roomId}")
    public Room deleteRoomById(@PathVariable("roomId") Long id) {
        return roomService.deleteRoomById(id);
    }

    @DeleteMapping
    public void deleteRoomById() {
        roomService.deleteAllRooms();
    }

    @DeleteMapping("/{roomId}/orders")
    public void deleteOrderByRoom(@PathVariable("roomId") Long id) {
        roomService.deleteOrdersByRoomId(id);
    }

}
