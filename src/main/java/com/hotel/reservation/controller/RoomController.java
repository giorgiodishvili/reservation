package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.adapter.RoomAdapter;
import com.hotel.reservation.config.security.authority.Authority;
import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.service.OrderService;
import com.hotel.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/rooms")
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
    public Page<Room> getRooms(Pageable pageable) {
        return roomService.getRooms(pageable);
    }

    @GetMapping("/{roomId}")
    public RoomAdapter getRoomById(@PathVariable("roomId") @Min(1) Long roomId) {
        return new RoomAdapter(roomService.getRoomById(roomId));
    }

    @GetMapping("/{roomId}/orders")
    @PreAuthorize(Authority.Order.READ)
    public Page<Order> getOrdersByRoom(@PathVariable("roomId") @Min(1) Long roomId, Pageable pageable) {
        return orderService.getOrdersByRoomId(roomId, pageable);
    }

    @PostMapping("/{roomId}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authority.Order.WRITE)
    public OrderAdapter createOrderByRoom(@RequestHeader Map<String, String> headers, @PathVariable("roomId") @Min(1) Long roomId, @RequestBody @Valid OrderAdapter orderAdapter) {
        String token = headers.get("Authorization");
        return orderService.createOrderByRoom(token, roomId, orderAdapter);
    }

    @PutMapping("/{roomId}/orders/{orderId}")
    @PreAuthorize(Authority.Order.UPDATE)
    public OrderAdapter updateOrderByRoom(@RequestHeader Map<String, String> headers, @PathVariable("roomId") @Min(1) Long roomId, @PathVariable("orderId") @Min(1) Long orderId, @RequestBody @Valid OrderAdapter orderAdapter) {
        String token = headers.get("Authorization");
        return orderService.updateOrderByRoomIdAndOrderId(token, roomId, orderId, orderAdapter);
    }

    @DeleteMapping("/{roomId}/orders/{orderId}")
    @PreAuthorize(Authority.Order.DELETE)
    public OrderAdapter deleteOrderByRoomAndOrderId(@PathVariable("roomId") @Min(1) Long roomId, @PathVariable("orderId") @Min(1) Long orderId) {
        return orderService.deleteOrderByRoomIdAndOrderId(roomId, orderId);
    }

}
