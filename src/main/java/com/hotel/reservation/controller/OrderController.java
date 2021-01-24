package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    @GetMapping("/{label}/{uuid}")
    public boolean checkOrder(@PathVariable("label") String label, @PathVariable("uuid") String UUID) {
        return orderService.checkOrder(label, UUID);
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

    @DeleteMapping
    public void deleteAllOrders() {
        orderService.deleteAllOrders();
    }

}
