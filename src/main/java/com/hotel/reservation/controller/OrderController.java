package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.ListAdapter;
import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.config.security.authority.Authority;
import com.hotel.reservation.entity.AppUser;
import com.hotel.reservation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize(Authority.Order.READ)
    public Page<OrderAdapter> getAllOrders(@AuthenticationPrincipal AppUser user, Pageable pageable) {
        return ListAdapter.createPageFromList(orderService.getAllOrders(user), pageable);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize(Authority.Order.READ)
    public OrderAdapter getOrderById(@AuthenticationPrincipal AppUser user, @PathVariable("orderId") @Min(1) Long orderId) {
        return orderService.getOrderById(user, orderId);
    }

    @GetMapping("/checkOrder")
    public boolean checkOrder(@RequestParam("roomLabel") String roomLabel, @RequestParam("uuid") @Size(min = 36, max = 36) String UUID) {
        return orderService.checkOrder(roomLabel, UUID);
    }
}
