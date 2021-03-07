package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.ListAdapter;
import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.adapter.RoomAdapter;
import com.hotel.reservation.config.security.authority.Authority;
import com.hotel.reservation.entity.AppUser;
import com.hotel.reservation.service.OrderService;
import com.hotel.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;


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
    public Page<RoomAdapter> getRooms(Pageable pageable) {
        return ListAdapter.createPageFromList(roomService.getRooms(pageable), pageable);
    }

    @GetMapping("/{roomId}")
    public RoomAdapter getRoomById(@PathVariable("roomId") @Min(1) Long roomId) {
        return new RoomAdapter(roomService.getRoomById(roomId));
    }

    @GetMapping("/{roomId}/orders")
    @PreAuthorize(Authority.Order.READ) //hasRole('ADMIN') DOESNT WORK
    public Page<OrderAdapter> getCurrentOrdersByRoomId(@AuthenticationPrincipal AppUser user, @PathVariable("roomId") @Min(1) Long roomId, Pageable pageable) {
        return ListAdapter.createPageFromList(orderService.getCurrentOrdersByRoomId(user, roomId), pageable);
    }

    @PostMapping("/{roomId}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authority.Order.WRITE)
    public OrderAdapter createOrderByRoom(@AuthenticationPrincipal AppUser user, @PathVariable("roomId") @Min(1) Long roomId, @RequestBody @Valid OrderAdapter orderAdapter) {
        return orderService.createOrderByRoom(user, roomId, orderAdapter);
    }

    @PutMapping("/{roomId}/orders/{orderId}")
    @PreAuthorize(Authority.Order.UPDATE)
    public OrderAdapter updateOrderByRoom(@AuthenticationPrincipal AppUser user, @PathVariable("roomId") @Min(1) Long roomId, @PathVariable("orderId") @Min(1) Long orderId, @RequestBody @Valid OrderAdapter orderAdapter) {
        return orderService.updateOrderByRoomIdAndOrderId(user, roomId, orderId, orderAdapter);
    }

    @DeleteMapping("/{roomId}/orders/{orderId}")
    @PreAuthorize(Authority.Order.DELETE)
    public OrderAdapter deleteOrderByRoomAndOrderId(@AuthenticationPrincipal AppUser user, @PathVariable("roomId") @Min(1) Long roomId, @PathVariable("orderId") @Min(1) Long orderId) {
        return orderService.deleteOrderByRoomIdAndOrderId(user, roomId, orderId);
    }

}
