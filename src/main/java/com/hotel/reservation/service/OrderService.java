package com.hotel.reservation.service;


import com.hotel.reservation.adapter.AppUserAdapter;
import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.config.security.authority.AppUserRole;
import com.hotel.reservation.config.security.jwt.JwtTokenProvider;
import com.hotel.reservation.entity.AppUser;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final AppUserService appUserService;
    private final RoomService roomService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OrderService(OrderRepository orderRepository, AppUserService appUserService, RoomService roomService, JwtTokenProvider jwtTokenProvider) {
        this.orderRepository = orderRepository;
        this.appUserService = appUserService;
        this.roomService = roomService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * @return List of Orders
     */
    public List<OrderAdapter> getAllOrders(AppUser appUser) {
        List<Order> all;
        if (appUser.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
            return orderRepository.findAll().stream()
                    .map(OrderAdapter::new)
                    .collect(Collectors.toList());
        }

        return findAllOrdersByUser(appUser.getId());


    }

    /**
     *
     * @param user    authenticated user
     * @param orderId id of an order
     * @return Orders
     * @throws OrderNotFoundException if orderId is not found
     */
    public OrderAdapter getOrderById(AppUser user, Long orderId) {
        OrderAdapter orderAdapter = new OrderAdapter(orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new));
        if (user.getAppUserRole() == AppUserRole.ROLE_ADMIN || user.getUsername().equals(orderAdapter.getUser())) {
            return orderAdapter;
        }

        throw new UsernameNotFoundException("This account is not authorized to view the data");
    }

    /**
     * @param user         extracted appUser
     * @param roomId       provided room id
     * @param orderAdapter provided order
     * @return Orders
     * @throws RoomNotFoundException if room is not found by room id
     */
    public OrderAdapter createOrderByRoom(AppUser user, Long roomId, OrderAdapter orderAdapter) {
        Room roomById = roomService.getRoomById(roomId);
        log.debug("Room by id is :{}", roomById);
        Order order = orderAdapter.toOrder();
        order.setRoom(roomById);
        order.setAppUser(user);

        return new OrderAdapter(checkingRequirementsOfOrderAndSaving(order));
    }

    /**
     * @param appUser      extracted app user
     * @param roomId       provided roomId
     * @param orderId      id of an order
     * @param orderAdapter updated version of a single order
     * @return OrderAdapter
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId is not found
     * @throws RoomNotFoundException  RoomNotFoundException if room is not found by provided id
     */
    public OrderAdapter updateOrderByRoomIdAndOrderId(AppUser appUser, Long roomId, Long orderId, OrderAdapter orderAdapter) {
        Order orderById = orderRepository.findByIdAndRoom(orderId, roomService.getRoomById(roomId)).orElseThrow(OrderNotFoundException::new);
        orderById.setDescription(orderAdapter.getDescription());
        orderById.setPeriodBegin(orderAdapter.getPeriodBegin());
        orderById.setPeriodEnd(orderAdapter.getPeriodEnd());

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal().;

        if (appUser.equals(orderById.getAppUser())) {
            orderById.setAppUser(appUser);
            return new OrderAdapter(checkingRequirementsOfOrderAndSaving(orderById));
        } else if (appUser.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
            return new OrderAdapter(checkingRequirementsOfOrderAndSaving(orderById));
        }

        throw new AccessDeniedException("This User can not modify order");
        /*
        alternative of the code
        requires to set updatable false parameter all @column that should not be updated
            if (!orderExistsById(orderId)) {
                throw new OrderNotFoundException();
            }
            Order order = orderAdapter.toOrder();
            order.setId(orderId);
         */
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
     * @return OrderAdapter
     * @throws OrderNotFoundException OrderNotFoundException if order by orderId and roomId is not found
     * @throws RoomNotFoundException  RoomNotFoundException if room is not found by provided id
     */
    public OrderAdapter deleteOrderByRoomIdAndOrderId(AppUser appUser, Long roomId, Long orderId) {

        Order orderById = orderRepository.findByIdAndRoom(orderId, roomService.getRoomById(roomId)).orElseThrow(OrderNotFoundException::new);
        log.debug("Order is :{}", orderById);

        if (appUser.equals(orderById.getAppUser()) || appUser.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
            orderRepository.deleteById(orderId);
            return new OrderAdapter(orderById);
        }

        throw new AccessDeniedException("This User can not modify order");

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
     * @param user   authenticated user
     * @param roomId provided room id
     * @return List of order
     * @throws RoomNotFoundException if room is not found by <code>roomId</code>
     */
    public List<OrderAdapter> getCurrentOrdersByRoomId(AppUser user, Long roomId) {
        Room roomById = roomService.getRoomById(roomId);

        log.debug("Room By ID is :{}", roomById);
        if (user.getAppUserRole() == AppUserRole.ROLE_ADMIN) {
            return orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(roomById, LocalDate.now())
                    .stream()
                    .map(OrderAdapter::new)
                    .collect(Collectors.toList());
        }

        throw new AccessDeniedException("This User can not modify order");
    }

    public List<OrderAdapter> findAllOrdersByUser(Long userId) {
        AppUserAdapter userById = appUserService.findUserById(userId);
        return orderRepository.findAllByAppUser(userById.toAppUser())
                .stream()
                .map(OrderAdapter::new)
                .collect(Collectors.toList());
    }
}
