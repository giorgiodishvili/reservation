package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Order> getAllOrders() {
        log.trace("executing getAllOrders in Order controller");
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable("orderId") Long orderId) {
        log.trace("executing getOrderById in Order controller");
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/{roomLabel}/{uuid}")
    public boolean checkOrder(@PathVariable("roomLabel") String roomLabel, @PathVariable("uuid") String UUID) {
        log.trace("executing checkOrder in Order controller");
        return orderService.checkOrder(roomLabel, UUID);
    }

    @PostMapping
    public Order createOrder(@RequestBody @Valid Order order) {
        log.trace("executing createOrder in Order controller");
        return orderService.createOrder(order);
    }

    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable("orderId") Long orderId, @RequestBody @Valid Order order) {
        log.trace("executing updateOrder in Order controller");
        return orderService.updateOrder(orderId, order);
    }

    @DeleteMapping("/{orderId}")
    public Order deleteOrderById(@PathVariable("orderId") Long orderId) {
        log.trace("executing deleteOrderById in Order controller");
        return orderService.deleteOrderById(orderId);
    }

}
