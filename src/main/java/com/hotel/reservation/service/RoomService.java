package com.hotel.reservation.service;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.OrderCanNotBeAddedException;
import com.hotel.reservation.exception.order.OrderNotFoundException;
import com.hotel.reservation.exception.order.OrderPlacedInPastException;
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
import java.util.Optional;

@Service
@Slf4j
public class RoomService {

    private final RoomRepository roomRepo;
    private final OrderRepository orderRepository;
    private final RoomTypeRepository roomTypeRepository;


    @Autowired
    public RoomService(RoomRepository roomRepo, OrderRepository orderRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepo = roomRepo;
        this.orderRepository = orderRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public Iterable<Room> getRooms() {
        log.info("in getRooms method ");
        return roomRepo.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);
    }

    public Room saveRoom(Room room) {
        roomTypeRepository.findByLabelOrId(room.getRoomType().getLabel(), room.getRoomType().getId()).orElseThrow(RoomTypeNotFoundException::new);
        Optional<Room> byLabel = roomRepo.findByLabel(room.getLabel());
        if (byLabel.isEmpty()) {
            return roomRepo.save(room);
        } else {
            throw new RoomLabelAlreadyExistsException();
        }
    }

    public Room deleteRoomById(Long id) {
        Room room = roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);
        List<Orders> allOrdersByRoom = orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now());

        if (!allOrdersByRoom.isEmpty()) {
            throw new RoomIsBusyException();
        }

        roomRepo.deleteById(id);
        return room;
    }

    /**
     * @param id
     * @param room
     * @return Room
     * @throws RoomNotFoundException           if..
     * @throws RoomLabelAlreadyExistsException if..
     */
    public Room updateRoomById(Long id, Room room) {
        roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);

        if (roomRepo.existsByLabelAndIdIsNot(room.getLabel(), id)) {
            throw new RoomLabelAlreadyExistsException();
        }
        room.setId(id);
        return roomRepo.save(room);
    }

    public void deleteAllRooms() {
        roomRepo.deleteAll();
    }

    public Orders saveOrder(Long id, Orders orders) {
        Room room = roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);
        boolean isTimeAvailable = orderRepository.existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(room, orders.getPeriodBegin(), orders.getPeriodEnd());

        if (!isTimeAvailable) {
            int difference = orders.getPeriodBegin().compareTo(LocalDate.now());
            boolean moreThanCurrentDate = difference >= 0;
            orders.setRoom(room);
            if (moreThanCurrentDate) {
                return orderRepository.save(orders);
            } else {
                throw new OrderPlacedInPastException();
            }
        } else {
            throw new OrderCanNotBeAddedException();
        }
    }


    public List<Orders> getOrdersByRoomId(Long id) {
        Room room = roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);

        List<Orders> allByRoomAndPeriod = orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now());
        if (allByRoomAndPeriod.isEmpty()) {
            throw new OrderNotFoundException();
        } else {
            return allByRoomAndPeriod;
        }
    }

    public void deleteOrdersByRoomId(Long id) {
        Room room = roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);

        orderRepository.deleteByRoom(room);
    }
}
