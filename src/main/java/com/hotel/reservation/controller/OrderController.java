package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


//TODO add logging


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Orders getOrderById(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/{roomLabel}/{uuid}")
    public boolean checkOrder(@PathVariable("roomLabel") String roomLabel, @PathVariable("uuid") String UUID) {
        return orderService.checkOrder(roomLabel, UUID);
    }

    @PostMapping
    public Orders createOrder(@RequestBody @Valid Orders order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{orderId}")
    public Orders updateOrder(@PathVariable("orderId") Long orderId, @RequestBody @Valid Orders order) {
        return orderService.updateOrder(orderId, order);
    }

    @DeleteMapping("/{orderId}")
    public Orders deleteOrderById(@PathVariable("orderId") Long orderId) {
        return orderService.deleteOrderById(orderId);
    }

}
