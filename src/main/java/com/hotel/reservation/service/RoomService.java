package com.hotel.reservation.service;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.exception.OrderCanNotBeAddedException;
import com.hotel.reservation.exception.order.exception.OrderNotFoundException;
import com.hotel.reservation.exception.room.exception.RoomIsBusyException;
import com.hotel.reservation.exception.room.exception.RoomLabelAlreadyExistsException;
import com.hotel.reservation.exception.room.exception.RoomNotFoundException;
import com.hotel.reservation.exception.room.type.exception.RoomTypeNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final OrderRepository orderRepository;
    private final RoomTypeRepository roomTypeRepository;


    @Autowired
    public RoomService(RoomRepository roomRepository, OrderRepository orderRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.orderRepository = orderRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
    }

    public Room saveRoom(Room room) {
        roomTypeRepository.findByLabelOrId(room.getRoomType().getLabel(), room.getRoomType().getId()).orElseThrow(RoomTypeNotFoundException::new);
        Optional<Room> byLabel = roomRepository.findByLabel(room.getLabel());
        if (byLabel.isEmpty()) {
            return roomRepository.save(room);
        } else {
            throw new RoomLabelAlreadyExistsException();
        }
    }

    public String deleteRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
        Optional<List<Orders>> allOrdersByRoom = orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now());

        if (allOrdersByRoom.get().isEmpty()) {
            roomRepository.deleteById(id);
            return "Student has been deleted";
        } else {
            throw new RoomIsBusyException();
        }
    }

    public Room updateRoomById(Long id, Room room) {
        roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
        Optional<Room> byLabel = roomRepository.findByLabel(room.getLabel());
        room.setId(id);
        if (byLabel.isEmpty() || (byLabel.get().getId().equals(id))) {

            return roomRepository.save(room);
        } else {
            throw new RoomLabelAlreadyExistsException();
        }
    }

    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }

    public Orders saveOrder(Long id, Orders orders) {
        Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
        Optional<List<Orders>> timeAvailableByRoomId = orderRepository.isTimeAvailableByRoomId(room, orders.getPeriodBegin(), orders.getPeriodEnd(), LocalDate.now());

        if (timeAvailableByRoomId.get().isEmpty()) {
            orders.setRoom(room);
            return orderRepository.save(orders);
        } else {
            throw new OrderCanNotBeAddedException();
        }
    }


    public List<Orders> getOrdersByRoomId(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);

        return orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now()).orElseThrow(OrderNotFoundException::new);
    }

    public void deleteOrdersByRoomId(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);

        orderRepository.deleteByRoom(room);
    }
}
