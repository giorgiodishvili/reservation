package com.hotel.reservation.service;


import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.OrderCanNotBeAddedException;
import com.hotel.reservation.exception.order.OrderIdMustBeZeroOrNullException;
import com.hotel.reservation.exception.order.OrderNotFoundException;
import com.hotel.reservation.exception.order.OrderPlacedInPastException;
import com.hotel.reservation.exception.room.RoomIdNotFoundException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


//TODO add logging


@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final RoomRepository roomRepo;

    @Autowired
    public OrderService(OrderRepository orderRepository, RoomRepository roomRepo) {
        this.orderRepository = orderRepository;
        this.roomRepo = roomRepo;
    }

    /**
     * @return Iterable of Orders
     */
    public Iterable<Orders> getAllOrders() {
        log.info("In getAllOrders method");
        return orderRepository.findAll();
    }

    /**
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException if orderId is not found
     */
    public Orders getOrderById(Long orderId) {
        log.info("In getOrderById method");
        log.debug("id is :{}", orderId);

        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    /**
     * @param orders single order which should be added
     * @return Orders
     * @throws OrderIdMustBeZeroOrNullException if order id is not zero or null
     */
    public Orders createOrder(Orders orders) {

        if (Objects.nonNull(orders.getId()) && 0L != orders.getId()) {
            log.error("Order ID must be zero or null");
            throw new OrderIdMustBeZeroOrNullException();
        }

        return saveToOrderRepo(orders);
    }

    /**
     * @param orderId id of an order
     * @param orders  updated version of a single order
     * @return Order
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     */
    public Orders updateOrder(Long orderId, Orders orders) {
        getOrderById(orderId);
        orders.setId(orderId);
        return saveToOrderRepo(orders);
    }

    /**
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     */
    public Orders deleteOrderById(Long orderId) {
        Orders orderById = getOrderById(orderId);
        orderRepository.deleteById(orderId);
        return orderById;
    }

    /**
     * @param roomLabel label of a room
     * @param UUID  uuid of the order
     * @return boolean
     * @throws RoomNotFoundException if room is not found by this label
     */
    public boolean checkOrder(String roomLabel, String UUID) {
        Room roomByLabel = roomRepo.findByLabel(roomLabel).orElseThrow(RoomNotFoundException::new);

        List<Orders> allByRoom = orderRepository.findAllByRoom(roomByLabel);

        for (Orders order : allByRoom) {
            if (order.getUuid().equals(UUID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param orders order which should be saved
     * @return Orders
     * @throws RoomIdNotFoundException     if order doesn't have room id
     * @throws RoomNotFoundException       if room doesnt exists by provided id
     * @throws OrderCanNotBeAddedException if room is not free
     * @throws OrderPlacedInPastException  if passed date is the in past
     */
    @NotNull
    private Orders saveToOrderRepo(Orders orders) {
        Long roomId = Optional.ofNullable(orders.getRoom().getId()).orElseThrow(RoomIdNotFoundException::new);
        Room roomById = roomRepo.findById(roomId).orElseThrow(RoomNotFoundException::new);
        orders.setRoom(roomById);

        log.debug("room roomId is :{}", roomId);
        boolean isRoomBusy = orderRepository.existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(orders.getRoom(), orders.getPeriodBegin(), orders.getPeriodEnd());

        if (isRoomBusy) {
            throw new OrderCanNotBeAddedException(); //Room is busy Exception xom ar jobia ?
        }

        int difference = orders.getPeriodBegin().compareTo(LocalDate.now());
        boolean moreThanCurrentDate = difference >= 0;

        if (!moreThanCurrentDate) {
            throw new OrderPlacedInPastException();
        }

        return orderRepository.save(orders);
    }
}
