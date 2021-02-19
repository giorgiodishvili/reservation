package com.hotel.reservation.service;


import com.hotel.reservation.entity.Order;
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
    public Iterable<Order> getAllOrders() {
        log.trace("executing getAllOrders");
        return orderRepository.findAll();
    }

    /**
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException if orderId is not found
     */
    public Order getOrderById(Long orderId) {
        log.trace("executing getOrderById");
        log.info("id is :{}", orderId);
        Order order = null;
        try{
            order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        }catch(OrderNotFoundException e){
            log.info("Order Id not found: ",e);
        }

        return order;
    }

    /**
     * @param order single order which should be added
     * @return Orders
     * @throws OrderIdMustBeZeroOrNullException if Order Id is not zero or null
     */
    public Order createOrder(Order order) {
        log.trace("executing createOrder");
        Order savedOrder = null;

        try{
            if (Objects.nonNull(order.getId()) && 0L != order.getId()) {
                throw new OrderIdMustBeZeroOrNullException();
            }

            savedOrder = saveToOrderRepo(order);
        }catch(OrderIdMustBeZeroOrNullException e){
            log.info("Order Id must be zero or null exception: ",e);
        }

        return savedOrder;
    }

    /**
     * @param orderId id of an order
     * @param order  updated version of a single order
     * @return Order
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     */
    public Order updateOrder(Long orderId, Order order) {
        getOrderById(orderId);
        order.setId(orderId);
        return saveToOrderRepo(order);
    }

    /**
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     */
    public Order deleteOrderById(Long orderId) {
        Order orderById = getOrderById(orderId);
        orderRepository.deleteById(orderId);
        return orderById;
    }

    /**
     * @param roomLabel label of a room
     * @param UUID      uuid of the order
     * @return boolean
     * @throws RoomNotFoundException if room is not found by this label
     */
    public boolean checkOrder(String roomLabel, String UUID) {
        Room roomByLabel = roomRepo.findByLabel(roomLabel).orElseThrow(RoomNotFoundException::new);

        List<Order> allByRoom = orderRepository.findAllByRoom(roomByLabel);

        /*
        აქ 1 მაინც order.getUuid().equals(UUID) თუ შესრულდება True უნდა დააბრუნო?
        ეს მგონი ის თემაა, თუ ჯავშანი "ახლა" ადევს ოთახს იმის UUID უნდა იყოს გადმოცემულ პარამერტს არა?
        და ყველა Order რომ არ არახუნო ბაზიდან, არ ჯობდა UUID-ით დაგეთრია Order? (ხომ ხვდები რომ 1 უნდა იყოს მაინც)
         */
        for (Order order : allByRoom) {
            if (order.getUuid().equals(UUID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param order order which should be saved
     * @return Orders
     * @throws RoomIdNotFoundException     if order doesn't have room id
     * @throws RoomNotFoundException       if room doesnt exists by provided id
     * @throws OrderCanNotBeAddedException if room is not free
     * @throws OrderPlacedInPastException  if passed date is the in past
     */
    // ეს @NotNull ანოტაცია რას გიგივარებს?
    @NotNull
    // ამის სახელს ვერ ჩავწვდი. saveToOrderRepo
    private Order saveToOrderRepo(Order order) {
        Long roomId = Optional.ofNullable(order.getRoom().getId()).orElseThrow(RoomIdNotFoundException::new);
        Room roomById = roomRepo.findById(roomId).orElseThrow(RoomNotFoundException::new);
        order.setRoom(roomById);

        log.debug("room roomId is :{}", roomId);
        boolean isRoomBusy = orderRepository.existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(order.getRoom(), order.getPeriodBegin(), order.getPeriodEnd());

        if (isRoomBusy) {
            throw new OrderCanNotBeAddedException(); //Room is busy Exception xom ar jobia ? კი, რატომაც არა.
        }

        int difference = order.getPeriodBegin().compareTo(LocalDate.now());
        if (difference >= 0) {
            throw new OrderPlacedInPastException();
        }
        return orderRepository.save(order);
    }
}
