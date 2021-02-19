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
    //ამ შემთხვევაში როგორ დავლოგო ? TRY Catch ? ორჯერ ხომ არ გამოვისვრი, არასწორი მგონია
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

            RoomIdMustBeZeroOrNullException roomIdMustBeZeroOrNullException = new RoomIdMustBeZeroOrNullException();

            //ასე უნდა დაილოგოს ექსეფშენები ?
            // ჰმმ. არა გიო. აქ რაც უნდა დაწერო არის info უფრო.
            // ექსეფშენი, ჯერ უნდა დაიჭირო, მერე რომ დალოგო.
            // შენს მიერ ნასროლი ერორი ერორად არ უნდა დალოგო.
            // ეგ რეალურად ერორი არაა შენთვის. მხოლოდ სხვისთვის.
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
     * @param roomId provided id
     * @return Room
     * @throws RoomNotFoundException if label exists and as well id is not room id
     * @throws RoomIsBusyException   if Room is Busy
     */
    public Room deleteRoomById(Long roomId) {
        Room room = getRoomById(roomId);

        // აქ EXISTSByRoomAndPeriodEndGreaterThanEqual მეთოდი რატო არ გამოიყენე და დაატარებ ამ Order-ებს ბაზიდან?
        List<Order> allOrderByRoom = orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now());

        if (!allOrderByRoom.isEmpty()) {
            RoomIsBusyException roomIsBusyException = new RoomIsBusyException();
            log.error("Room is Busy ", roomIsBusyException);
            throw roomIsBusyException;
        }
        roomRepo.deleteById(roomId);
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
        getRoomById(roomId);

        if (roomRepo.existsByLabelAndIdIsNot(room.getLabel(), roomId)) {
            RoomLabelAlreadyExistsException roomLabelAlreadyExistsException = new RoomLabelAlreadyExistsException();
            log.error("Room Label Already exists ", roomLabelAlreadyExistsException);

            throw roomLabelAlreadyExistsException;
        }
        room.setId(roomId);
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
        return orderRepository.findAllByRoomAndPeriodEndGreaterThanEqual(roomById, LocalDate.now());
    }

}
