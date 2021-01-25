package com.hotel.reservation.service;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.order.OrderNotFoundException;
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
import java.util.Optional;


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


    public Iterable<Room> getRooms() {
        return roomRepo.findAll();
    }

    //ამ შემთხვევაში როგორ დავლოგო ? TRY Catch ? ორჯერ ხომ არ გამოვისვრი, არასწორი მგონია
    public Room getRoomById(Long id) {
        return roomRepo.findById(id).orElseThrow(RoomNotFoundException::new);
    }


    public Room createRoom(Room room) {

        if (Objects.nonNull(room.getId()) && 0L != room.getId()) {

            RoomIdMustBeZeroOrNullException roomIdMustBeZeroOrNullException = new RoomIdMustBeZeroOrNullException();

            //ასე უნდა დაილოგოს ექსეფშენები ?
            log.error("Room ID is :{}", room.getId(), roomIdMustBeZeroOrNullException);

            throw roomIdMustBeZeroOrNullException;
        }

        roomTypeRepository.findByLabelOrId(room.getRoomType().getLabel(), room.getRoomType().getId()).orElseThrow(RoomTypeNotFoundException::new);
        Optional<Room> roomByLabel = roomRepo.findByLabel(room.getLabel());

        if (roomByLabel.isPresent()) {
            RoomLabelAlreadyExistsException roomLabelAlreadyExistsException = new RoomLabelAlreadyExistsException();
            log.error("Room Label :{} Already exists ", roomByLabel.get().getLabel(), roomLabelAlreadyExistsException);
            throw roomLabelAlreadyExistsException;
        }

        return roomRepo.save(room);
    }

    /**
     * @param id provided id
     * @return Room
     * @throws RoomNotFoundException if label exists and as well id is not room id
     * @throws RoomIsBusyException   if Room is Busy
     */
    public Room deleteRoomById(Long id) {
        Room room = getRoomById(id);

        List<Orders> allOrdersByRoom = orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now());

        if (!allOrdersByRoom.isEmpty()) {
            RoomIsBusyException roomIsBusyException = new RoomIsBusyException();
            log.error("Room is Busy ", roomIsBusyException);
            throw roomIsBusyException;
        }

        roomRepo.deleteById(id);
        return room;
    }

    /**
     * @param id   provided id
     * @param room provided Room
     * @return Room
     * @throws RoomNotFoundException           if label exists and as well id is not room id
     * @throws RoomLabelAlreadyExistsException if room label exists
     */
    public Room updateRoomById(Long id, Room room) {
        getRoomById(id);

        if (roomRepo.existsByLabelAndIdIsNot(room.getLabel(), id)) {
            RoomLabelAlreadyExistsException roomLabelAlreadyExistsException = new RoomLabelAlreadyExistsException();

            log.error("Room Label Already exists ", roomLabelAlreadyExistsException);

            throw roomLabelAlreadyExistsException;
        }
        room.setId(id);
        return roomRepo.save(room);
    }

    public void deleteAllRooms() {
        roomRepo.deleteAll();
    }

    public Orders saveOrder(Long id, Orders order) {
        Room roomById = getRoomById(id);
        order.setRoom(roomById);
        return orderService.createOrder(order);
    }


    public List<Orders> getOrdersByRoomId(Long id) {
        Room roomById = getRoomById(id);

        List<Orders> allByRoomAndPeriod = orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(roomById, LocalDate.now());

        if (allByRoomAndPeriod.isEmpty()) {
            throw new OrderNotFoundException();
        }
        return allByRoomAndPeriod;

    }

    public void deleteOrdersByRoomId(Long id) {
        Room roomById = getRoomById(id);

        orderRepository.deleteByRoom(roomById);
    }

}
