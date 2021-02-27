package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable("orderId") @Min(1) Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/checkOrder")
    public boolean checkOrder(@RequestParam("roomLabel") String roomLabel, @RequestParam("uuid") @Size(min = 36, max = 36) String UUID) {
        return orderService.checkOrder(roomLabel, UUID);
    }
}
