package com.hotel.reservation.service;


import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.OrderNotFoundException;
import com.hotel.reservation.exception.order.OrderPlacedInPastException;
import com.hotel.reservation.exception.room.RoomIdNotFoundException;
import com.hotel.reservation.exception.room.RoomIsBusyException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


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

    /**
     * @return Iterable of Orders
     */
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
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
     * @param roomId       provided room id
     * @param orderAdapter provided order
     * @return Orders
     * @throws RoomNotFoundException if room is not found by room id
     */
    public Order createOrderByRoom(Long roomId, OrderAdapter orderAdapter) {
        Room roomById = roomService.getRoomById(roomId);
        log.debug("Room by id is :{}", roomById);
        Order order = orderAdapter.toOrder();
        order.setRoom(roomById);

        return checkingRequirementsOfOrderAndSaving(order);
    }

    /**
     * @param roomId       provided roomId
     * @param orderId      id of an order
     * @param orderAdapter updated version of a single order
     * @return OrderAdapter
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     * @throws RoomNotFoundException  RoomNotFoundException if room is not found by provided id
     */
    public Order updateOrderByRoomIdAndOrderId(Long roomId, Long orderId, OrderAdapter orderAdapter) {
        Order orderById = orderRepository.findByIdAndRoom(orderId, roomService.getRoomById(roomId)).orElseThrow(OrderNotFoundException::new);
        orderById.setDescription(orderAdapter.getDescription());
        orderById.setPeriodBegin(orderAdapter.getPeriodBegin());
        orderById.setPeriodEnd(orderAdapter.getPeriodEnd());

        /*
        alternative of the code
        requires to set updatable false parameter all @column that should not be updated
            if (!orderExistsById(orderId)) {
                throw new OrderNotFoundException();
            }
            Order order = orderAdapter.toOrder();
            order.setId(orderId);
         */
        return checkingRequirementsOfOrderAndSaving(orderById);
    }

    /**
     * @param order order which should be saved
     * @return Order
     * @throws RoomIdNotFoundException    if order doesn't have room id
     * @throws RoomNotFoundException      if room doesnt exists by provided id
     * @throws RoomIsBusyException        if room is not free
     * @throws OrderPlacedInPastException if passed date is the in past
     */
    private Order checkingRequirementsOfOrderAndSaving(Order order) {
        boolean isRoomBusy = orderRepository.existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(order.getRoom(), order.getPeriodBegin(), order.getPeriodEnd());

        if (isRoomBusy) {
            throw new RoomIsBusyException();
        }

        if (order.getPeriodBegin().compareTo(LocalDate.now()) < 0) {
            throw new OrderPlacedInPastException();
        }

        return orderRepository.save(order);
    }

    /**
     * @param orderId id of an order
     * @param roomId  provided roomId
     * @return Orders
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId and roomId is not found
     * @throws RoomNotFoundException  RoomNotFoundException if room is not found by provided id
     */
    public Order deleteOrderByRoomIdAndOrderId(Long roomId, Long orderId) {

        Order orderById = orderRepository.findByIdAndRoom(orderId, roomService.getRoomById(roomId)).orElseThrow(OrderNotFoundException::new);
        log.debug("Order is :{}", orderById);
        orderRepository.deleteById(orderId);

        return orderById;
    }


    /**
     * @param roomLabel label of a room
     * @param UUID      uuid of the order
     * @return boolean
     * @throws RoomNotFoundException  if room is not found by <code>roomLabel</code>
     * @throws OrderNotFoundException if room is not found by <code>UUID</code>
     */
    public boolean checkOrder(String roomLabel, String UUID) {

        Order order = findOrderByUuid(UUID);

        if (!roomLabel.equals(order.getRoom().getLabel())) {
            throw new RoomNotFoundException();
        }
        return order.getPeriodBegin().compareTo(LocalDate.now()) * LocalDate.now().compareTo(order.getPeriodEnd()) >= 0;
    }

    /**
     * @param uuid provided order uuid
     * @return Order
     * @throws OrderNotFoundException if order is not found by uuid
     */
    public Order findOrderByUuid(String uuid) {
        return orderRepository.findByUuid(uuid).orElseThrow(OrderNotFoundException::new);
    }

    /**
     * @param roomId provided room id
     * @return List of order
     * @throws RoomNotFoundException if room is not found by <code>roomId</code>
     */
    public Page<Order> getOrdersByRoomId(Long roomId, Pageable pageable) {
        Room roomById = roomService.getRoomById(roomId);
        log.debug("Room By ID is :{}", roomById);
        return orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(roomById, LocalDate.now(),pageable);
    }
}
