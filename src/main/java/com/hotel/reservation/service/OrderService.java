package com.hotel.reservation.service;


import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.exception.order.OrderNotFoundException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.RoomTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final RoomTypeRepository roomTypeRepository;
    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @Autowired
    public OrderService(RoomTypeRepository roomTypeRepository, OrderRepository orderRepository, RoomRepository roomRepository, RoomService roomService) {
        this.roomTypeRepository = roomTypeRepository;
        this.orderRepository = orderRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    public Iterable<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public Orders saveOrder(Orders orders) {
        Long id = Optional.ofNullable(orders.getRoom().getId()).orElseThrow(RoomNotFoundException::new);
        log.debug("room id is :{}", id);
        return roomService.saveOrder(id, orders);
    }

    public Orders updateOrder(Long id, Orders orders) {
        Orders orderById = getOrderById(id);
        orders.setId(id);
        return saveOrder(orders);
    }

    public String deleteOrderById(Long id) {
        getOrderById(id);
        orderRepository.deleteById(id);
        return "Order Has been deleted";
    }


    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
