package com.hotel.reservation.service;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.room.RoomIdMustBeZeroOrNullException;
import com.hotel.reservation.exception.room.RoomIsBusyException;
import com.hotel.reservation.exception.room.RoomLabelAlreadyExistsException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.exception.type.RoomTypeNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.RoomTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class RoomService {

    private final RoomRepository roomRepo;
    private final OrderRepository orderRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final OrderService orderService;


    @Autowired
    public RoomService(RoomRepository roomRepo, OrderRepository orderRepository, RoomTypeRepository roomTypeRepository, OrderService orderService) {
        this.roomRepo = roomRepo;
        this.orderRepository = orderRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.orderService = orderService;
    }


    /**
     * @return Iterable of Room
     */
    public Iterable<Room> getRooms() {
        return roomRepo.findAll();
    }

    /**
     * @param roomId provided roomId
     * @return Room
     * @throws RoomNotFoundException if room by roomId is not found
     */
    public Room getRoomById(Long roomId) {
        return roomRepo.findById(roomId).orElseThrow(RoomNotFoundException::new);
    }

    /**
     * @param room provided room
     * @return Room
     * @throws RoomIdMustBeZeroOrNullException if room id is not zero or null
     * @throws RoomTypeNotFoundException       if room type of specified room  is null
     * @throws RoomLabelAlreadyExistsException if room label already exists than room cant be saved
     */
    public Room createRoom(Room room) {
        if (Objects.nonNull(room.getId()) && 0L != room.getId()) {
            throw new RoomIdMustBeZeroOrNullException();
        }

        if (!roomTypeRepository.existsByLabelOrId(room.getRoomType().getLabel(), room.getRoomType().getId())) {
            throw new RoomTypeNotFoundException();
        }

        if (roomExistsByLabel(room.getLabel())) {
            throw new RoomLabelAlreadyExistsException();
        }

        return roomRepo.save(room);
    }

    /**
     * @param roomId provided id
     * @return Room
     * @throws RoomNotFoundException if label exists and as well id is not room id
     * @throws RoomIsBusyException   if Room is Busy
     */
    public Room deleteRoomById(Long roomId) {
        Room room = getRoomById(roomId);

        if (orderRepository.existsByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now())) {
            throw new RoomIsBusyException();
        }

        roomRepo.deleteById(roomId);
        log.debug("Deleted Room :{}", room);

        return room;
    }

    /**
     * @param roomId provided id
     * @param room   provided Room
     * @return Room
     * @throws RoomNotFoundException           if label exists and as well id is not room id
     * @throws RoomLabelAlreadyExistsException if room label exists
     */
    public Room updateRoomById(Long roomId, Room room) {
        room.setId(roomId);
        if (roomExistsByRoomId(roomId)) {
            throw new RoomNotFoundException();
        }

        if (roomExistsByRoomLabelAndRoomIdIsNot(room.getLabel(), room.getId())) {
            throw new RoomLabelAlreadyExistsException();
        }

        return roomRepo.save(room);
    }


    /**
     * @param roomId provided room id
     * @param order  provided order
     * @return Orders
     * @throws RoomNotFoundException if room is not found by room id
     */
    public Order createOrder(Long roomId, Order order) {
        Room roomById = getRoomById(roomId);
        log.debug("Room by id is :{}", roomById);

        order.setRoom(roomById);
        return orderService.createOrder(order);
    }

    /**
     * @param roomId provided room id
     * @return List of order
     * @throws RoomNotFoundException if room is not found by <code>roomId</code>
     */
    public List<Order> getOrdersByRoomId(Long roomId) {
        Room roomById = getRoomById(roomId);
        log.debug("Room By ID is :{}", roomById);
        return orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(roomById, LocalDate.now());
    }

    /**
     * @param roomLabel provided room label
     * @return boolean
     */
    public boolean roomExistsByLabel(String roomLabel) {
        return roomRepo.existsByLabel(roomLabel);
    }

    /**
     * @param roomId provided room id
     * @return boolean
     */
    public boolean roomExistsByRoomId(Long roomId) {
        return roomRepo.existsById(roomId);
    }

    /**
     * @param roomLabel provided Room Label
     * @param roomId    provided Room ID
     * @return boolean
     */
    public boolean roomExistsByRoomLabelAndRoomIdIsNot(String roomLabel, Long roomId) {
        return roomRepo.existsByLabelAndIdIsNot(roomLabel, roomId);
    }
}
