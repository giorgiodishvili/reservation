package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


//TODO add logging


@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/{roomLabel}/{uuid}")
    public boolean checkOrder(@PathVariable("roomLabel") String roomLabel, @PathVariable("uuid") String UUID) {
        return orderService.checkOrder(roomLabel, UUID);
    }

    @PostMapping
    public Order createOrder(@RequestBody @Valid Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable("orderId") Long orderId, @RequestBody @Valid Order order) {
        return orderService.updateOrder(orderId, order);
    }

    @DeleteMapping("/{orderId}")
    public Order deleteOrderById(@PathVariable("orderId") Long orderId) {
        return orderService.deleteOrderById(orderId);
    }

}
