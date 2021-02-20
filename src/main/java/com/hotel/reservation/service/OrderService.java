package com.hotel.reservation.service;


import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.OrderIdMustBeZeroOrNullException;
import com.hotel.reservation.exception.order.OrderNotFoundException;
import com.hotel.reservation.exception.order.OrderPlacedInPastException;
import com.hotel.reservation.exception.room.RoomIdNotFoundException;
import com.hotel.reservation.exception.room.RoomIsBusyException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;


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
        return orderRepository.findAll();
    }

    /**
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException if orderId is not found
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    /**
     * @param order single order which should be added
     * @return Orders
     * @throws OrderIdMustBeZeroOrNullException if Order Id is not zero or null
     */
    public Order createOrder(Order order) {
        if (Objects.nonNull(order.getId()) && 0L != order.getId()) {
            throw new OrderIdMustBeZeroOrNullException();
        }


        return checkingRequirementsOfOrderBeforeSaving(order);
    }

    /**
     * @param orderId id of an order
     * @param order   updated version of a single order
     * @return Order
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     */
    public Order updateOrder(Long orderId, Order order) {
        order.setId(orderId);
        if (orderExistsById(orderId)) {
            throw new OrderNotFoundException();
        }

        return checkingRequirementsOfOrderBeforeSaving(order);
    }

    /**
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     */
    public Order deleteOrderById(Long orderId) {

        Order orderById = getOrderById(orderId);
        log.debug("Order is :{}", orderById);
        orderRepository.deleteById(orderId);

        return orderById;
    }

    /**
     * @param roomLabel label of a room
     * @param UUID      uuid of the order
     * @return boolean
     * @throws RoomNotFoundException if room is not found by <code>roomLabel</code>
     * @throws OrderNotFoundException if room is not found by <code>UUID</code>
     */
    public boolean checkOrder(String roomLabel, String UUID) {
        if(!roomRepo.existsByLabel(roomLabel)){
            throw new RoomNotFoundException();
        }

        Order order = findOrderByUuid(UUID);
        return LocalDate.now().compareTo(order.getPeriodBegin()) >= 0 &&  LocalDate.now().compareTo(order.getPeriodEnd()) >= 0 && roomLabel.equals(order.getRoom().getLabel());
    }

    /**
     * @param order order which should be saved
     * @return Orders
     * @throws RoomIdNotFoundException    if order doesn't have room id
     * @throws RoomNotFoundException      if room doesnt exists by provided id
     * @throws RoomIsBusyException        if room is not free
     * @throws OrderPlacedInPastException if passed date is the in past
     */
    private Order checkingRequirementsOfOrderBeforeSaving(Order order) {
        Long roomId = Optional.ofNullable(order.getRoom().getId()).orElseThrow(RoomIdNotFoundException::new);
        log.debug("Room Id is :{}", roomId);

        Room roomById = roomRepo.findById(roomId).orElseThrow(RoomNotFoundException::new);
        log.debug("Room By Id is :{}", roomById);

        order.setRoom(roomById);
        boolean isRoomBusy = orderRepository.existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(order.getRoom(), order.getPeriodBegin(), order.getPeriodEnd());

        if (isRoomBusy) {
            throw new RoomIsBusyException();
        }

        int difference = order.getPeriodBegin().compareTo(LocalDate.now());
        if (difference >= 0) {
            throw new OrderPlacedInPastException();
        }
        return orderRepository.save(order);
    }

    /**
     * @param orderId provided room id
     * @return boolean
     */
    public boolean orderExistsById(Long orderId) {
        return orderRepository.existsById(orderId);
    }

    /**
     *
     * @param uuid provided order uuid
     * @return Order
     * @throws OrderNotFoundException if order is not found by uuid
     */
    public Order findOrderByUuid(String uuid){
        return orderRepository.findByUuid(uuid).orElseThrow(OrderNotFoundException::new);
    }
}
