package com.hotel.reservation.service;


import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.OrderCanNotBeAddedException;
import com.hotel.reservation.exception.order.OrderNotFoundException;
import com.hotel.reservation.exception.order.OrderPlacedInPastException;
import com.hotel.reservation.exception.room.RoomIdMustBeZeroOrNullException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final RoomService roomService;

    @Autowired
    public OrderService(OrderRepository orderRepository, RoomService roomService) {
        this.orderRepository = orderRepository;
        this.roomService = roomService;
    }

    public Iterable<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public Orders createOrder(Orders orders) {

        if (Objects.nonNull(orders.getId()) && 0L != orders.getId()) {
            log.error("Order ID must be zero or null");
            throw new RoomIdMustBeZeroOrNullException();
        }

        return saveToOrderRepo(orders);
    }


    public Orders updateOrder(Long id, Orders orders) {
        getOrderById(id);
        orders.setId(id);
        return saveToOrderRepo(orders);
    }

    public Orders deleteOrderById(Long id) {
        Orders orderById = getOrderById(id);
        orderRepository.deleteById(id);
        return orderById;
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    public boolean checkOrder(String label, String UUID) {
        Room roomByLabel = roomService.getRoomByLabel(label);

        List<Orders> allByRoom = orderRepository.findAllByRoom(roomByLabel);

        for (Orders order : allByRoom) {
            if (order.getUuid().equals(UUID)) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    private Orders saveToOrderRepo(Orders orders) {
        Long roomId = Optional.ofNullable(orders.getRoom().getId()).orElseThrow(RoomNotFoundException::new);
        Room roomById = roomService.getRoomById(roomId);
        orders.setRoom(roomById);

        log.debug("room roomId is :{}", roomId);
        boolean isRoomBusy = orderRepository.existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(orders.getRoom(), orders.getPeriodBegin(), orders.getPeriodEnd());

        if (isRoomBusy) {
            throw new OrderCanNotBeAddedException();
        }

        int difference = orders.getPeriodBegin().compareTo(LocalDate.now());
        boolean moreThanCurrentDate = difference >= 0;

        if (!moreThanCurrentDate) {
            throw new OrderPlacedInPastException();
        }

        return orderRepository.save(orders);
    }
}
