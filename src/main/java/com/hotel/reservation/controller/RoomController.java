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
        log.trace("executing getRooms in Room controller");
        return roomService.getRooms();
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable("roomId") Long roomId) {
        log.trace("executing getRoomById in Room controller");
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/{roomId}/order") // არადა orders სწორი იყო. :(
    public List<Order> getOrders(@PathVariable("roomId") Long roomId) {
        log.trace("executing getOrders in Room controller");
        return roomService.getOrdersByRoomId(roomId);
    }

    @PostMapping
    public Room saveRoom(@RequestBody @Valid Room room) {
        log.trace("executing saveRoom in Room controller");
        return roomService.createRoom(room);
    }


    @PostMapping("/{roomId}/order") // არადა orders სწორი იყო. :(
    public Order saveOrder(@PathVariable("roomId") Long roomId, @RequestBody @Valid Order order) {
        log.trace("executing saveOrder in Room controller");
        return roomService.createOrder(roomId, order);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable("roomId") Long id, @RequestBody @Valid Room room) {
        log.trace("executing updateRoom in Room controller");
        return roomService.updateRoomById(id, room);
    }

    @DeleteMapping("/{roomId}")
    public Room deleteRoomById(@PathVariable("roomId") Long roomId) {
        log.trace("executing deleteRoomById in Room controller");
        return roomService.deleteRoomById(roomId);
    }


}
