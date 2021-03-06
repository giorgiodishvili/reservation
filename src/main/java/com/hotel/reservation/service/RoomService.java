package com.hotel.reservation.service;

import com.hotel.reservation.adapter.RoomAdapter;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.exception.room.RoomIsBusyException;
import com.hotel.reservation.exception.room.RoomLabelAlreadyExistsException;
import com.hotel.reservation.exception.room.RoomNotFoundException;
import com.hotel.reservation.exception.type.RoomTypeNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@Slf4j
public class RoomService {

    private final RoomRepository roomRepo;
    private final OrderRepository orderRepository;
    private final RoomTypeService roomTypeService;


    @Autowired
    public RoomService(RoomRepository roomRepo, OrderRepository orderRepository, RoomTypeService roomTypeService) {
        this.roomRepo = roomRepo;
        this.orderRepository = orderRepository;
        this.roomTypeService = roomTypeService;
    }


    /**
     * @return Iterable of Room
     */
    public Page<Room> getRooms(Pageable pageable) {
        return roomRepo.findAll(pageable);
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
     * @param roomAdapter provided room
     * @return Room
     * @throws RoomTypeNotFoundException       if room type of specified room  is null
     * @throws RoomLabelAlreadyExistsException if room label already exists than room cant be saved
     */
    public RoomAdapter createRoomByRoomType(Long roomTypeId, RoomAdapter roomAdapter) {
        RoomType roomTypeById = roomTypeService.getRoomTypeById(roomTypeId);

        if (roomExistsByLabel(roomAdapter.getLabel())) {
            throw new RoomLabelAlreadyExistsException();
        }

        Room room = roomAdapter.toRoom();
        room.setRoomType(roomTypeById);


        return new RoomAdapter(roomRepo.save(room));
    }

    /**
     * @param roomId provided id
     * @return Room
     * @throws RoomNotFoundException if label exists and as well id is not room id
     * @throws RoomIsBusyException   if Room is Busy
     */
    public RoomAdapter deleteRoomById(Long roomTypeId, Long roomId) {
        Room room = getRoomByIdAndRoomTypeId(roomId, roomTypeId);

        if (orderRepository.existsByRoomAndPeriodEndGreaterThanEqual(room, LocalDate.now())) {
            throw new RoomIsBusyException();
        }

        roomRepo.deleteById(roomId);
        log.debug("Deleted Room :{}", room);

        return new RoomAdapter(room);
    }

    public Room getRoomByIdAndRoomTypeId(Long roomId, Long roomTypeId) {
        return roomRepo.findByIdAndRoomType(roomId, roomTypeService.getRoomTypeById(roomTypeId)).orElseThrow(RoomNotFoundException::new);
    }

    /**
     * @param roomId      provided id
     * @param roomAdapter provided Room
     * @return Room
     * @throws RoomNotFoundException           if label exists and as well id is not room id
     * @throws RoomLabelAlreadyExistsException if room label exists
     */
    public RoomAdapter updateRoomByRoomTypeAndRoomId(Long roomTypeId, Long roomId, RoomAdapter roomAdapter) {
        if (!roomExistsByRoomId(roomId)) {
            throw new RoomNotFoundException();
        }
        RoomType roomTypeById = roomTypeService.getRoomTypeById(roomTypeId);
        Room room = roomAdapter.toRoom();
        room.setId(roomId);
        room.setRoomType(roomTypeById);
        if (roomExistsByRoomLabelAndRoomIdIsNot(room.getLabel(), room.getId())) {
            throw new RoomLabelAlreadyExistsException();
        }

        return new RoomAdapter(roomRepo.save(room));
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
