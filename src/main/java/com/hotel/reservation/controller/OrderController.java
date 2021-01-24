package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public Iterable<Orders> getRooms() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Orders getRoomById(@PathVariable("orderId") Long id) {
        return orderService.getOrderById(id);
    }

//    @GetMapping("/{roomId}/orders")
//    public List<Orders> getOrders(@PathVariable("roomId") Long id) {
//        return orderService.getOrdersByRoomId(id);
//    }

    @PostMapping("/")
    public Orders saveRoom(@RequestBody Orders order) {
        return orderService.saveOrder(order);
    }


    @PutMapping("/{orderId}")
    public Orders updateRoom(@PathVariable("orderId") Long id, @RequestBody Orders order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{orderId}")
    public String deleteRoomById(@PathVariable("orderId") Long id) {
        return orderService.deleteOrderById(id);
    }

    @DeleteMapping("/")
    public void deleteAllOrders() {
        orderService.deleteAllOrders();
    }

}
