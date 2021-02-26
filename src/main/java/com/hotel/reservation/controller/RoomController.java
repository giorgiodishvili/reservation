package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.service.OrderService;
import com.hotel.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(value = "/rooms")
@Validated
public class RoomController {

    private final RoomService roomService;
    private final OrderService orderService;

    @Autowired
    public RoomController(RoomService roomService, OrderService orderService) {
        this.roomService = roomService;
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable("roomId") @Min(1) Long roomId) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/{roomId}/orders")
    public List<Order> getOrdersByRoom(@PathVariable("roomId") @Min(1) Long roomId) {
        return orderService.getOrdersByRoomId(roomId);
    }

    @PostMapping("/{roomId}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrderByRoom(@PathVariable("roomId") @Min(1) Long roomId, @RequestBody @Valid OrderAdapter orderAdapter) {
        return orderService.createOrderByRoom(roomId, orderAdapter);
    }

    @PutMapping("/{roomId}/orders/{orderId}")
    public Order updateOrderByRoom(@PathVariable("roomId") @Min(1) Long roomId, @PathVariable("orderId") @Min(1) Long orderId, @RequestBody @Valid OrderAdapter orderAdapter) {
        return orderService.updateOrderByRoomIdAndOrderId(roomId, orderId, orderAdapter);
    }

    @DeleteMapping("/{roomId}/orders/{orderId}")
    public Order deleteOrderByRoomAndOrderId(@PathVariable("roomId") @Min(1) Long roomId, @PathVariable("orderId") @Min(1) Long orderId) {
        return orderService.deleteOrderByRoomIdAndOrderId(roomId, orderId);
    }

}
