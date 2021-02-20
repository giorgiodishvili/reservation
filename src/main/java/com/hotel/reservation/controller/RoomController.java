package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/rooms")
@Slf4j
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
    public Room getRoomById(@PathVariable("roomId") Long roomId) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/{roomId}/orders")
    public List<Order> getOrders(@PathVariable("roomId") Long roomId) {
        return roomService.getOrdersByRoomId(roomId);
    }

    @PostMapping
    public Room saveRoom(@RequestBody @Valid Room room) {
        return roomService.createRoom(room);
    }


    @PostMapping("/{roomId}/orders")
    public Order saveOrder(@PathVariable("roomId") Long roomId, @RequestBody @Valid Order order) {
        return roomService.createOrder(roomId, order);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable("roomId") Long id, @RequestBody @Valid Room room) {
        return roomService.updateRoomById(id, room);
    }

    @DeleteMapping("/{roomId}")
    public Room deleteRoomById(@PathVariable("roomId") Long roomId) {
        return roomService.deleteRoomById(roomId);
    }


}
