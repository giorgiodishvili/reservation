package com.hotel.reservation.service;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.exception.type.RoomTypeIdMustBeZeroOrNullException;
import com.hotel.reservation.exception.type.RoomTypeIsUsedException;
import com.hotel.reservation.exception.type.RoomTypeLabelAlreadyExistsException;
import com.hotel.reservation.exception.type.RoomTypeNotFoundException;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.RoomTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;


    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, RoomRepository roomRepository, RoomService roomService) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    /**
     * @return Iterable of Room Type
     */
    public Iterable<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    /**
     * @param roomTypeId provided room type id
     * @return RoomType
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public RoomType getRoomTypeById(Long roomTypeId) {
        return roomTypeRepository.findById(roomTypeId).orElseThrow(RoomTypeNotFoundException::new);
    }

    /**
     * @param roomType provided room Type
     * @return RoomType
     * @throws RoomTypeIdMustBeZeroOrNullException if room type id is not zero or null
     * @throws RoomTypeLabelAlreadyExistsException if room type label already exists room type cant be added
     */
    public RoomType createRoomType(RoomType roomType) {

        if (Objects.nonNull(roomType.getId()) && 0L != roomType.getId()) {
            throw new RoomTypeIdMustBeZeroOrNullException();
        }

        boolean existsByLabel = roomTypeExistsByLabel(roomType.getLabel());

        log.debug("Room Type Label Exists: :{}", existsByLabel);

        if (existsByLabel) {
            throw new RoomTypeLabelAlreadyExistsException();
        }

        return roomTypeRepository.save(roomType);
    }

    /**
     * @param roomTypeId provided room type id
     * @return RoomType
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     * @throws RoomTypeIsUsedException   if room type is used
     */
    public RoomType deleteRoomTypeById(Long roomTypeId) {

        RoomType roomType = getRoomTypeById(roomTypeId);
        log.debug("RoomType is :{}", roomType);

        boolean roomIsPresent = roomExistsByRoomType(roomType);
        log.debug("Room exists by Room Type :{}", roomIsPresent);

        if (!roomIsPresent) {
            throw new RoomTypeIsUsedException();
        }

        roomTypeRepository.deleteById(roomTypeId);

        return roomType;
    }

    /**
     * @param roomTypeId provided room type id
     * @param roomType   provided roomType
     * @return RoomType
     * @throws RoomTypeNotFoundException           if room type is not found by room type id
     * @throws RoomTypeLabelAlreadyExistsException if room type label already exists room type cant be updated
     */
    public RoomType updateRoomTypeById(Long roomTypeId, RoomType roomType) {

        if (!roomTypeExistsById(roomTypeId)) {
            throw new RoomTypeNotFoundException();
        }

        roomType.setId(roomTypeId);

        if (roomTypeExistsByRoomTypeLabelAndRoomTypeIdIsNot(roomType.getLabel(), roomType.getId())) {
            throw new RoomTypeLabelAlreadyExistsException();
        }

        return roomTypeRepository.save(roomType);
    }

    /**
     * @param roomTypeId provided room type if
     * @param room       provided room
     * @return Room
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public Room createRoomByRoomType(Long roomTypeId, Room room) {
        RoomType roomType = getRoomTypeById(roomTypeId);
        log.debug("Room type is :{}", roomType);

        room.setRoomType(roomType);
        log.debug("Room is :{}", room);

        return roomService.createRoom(room);
    }

    /**
     * @param roomTypeId provided room type id
     * @return List of Rooms
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public List<Room> getAllRoomsByRoomTypeId(Long roomTypeId) {
        RoomType roomType = getRoomTypeById(roomTypeId);

        log.debug("Room type is :{}", roomType);
        return roomRepository.findByRoomType(roomType);
    }

    /**
     * @param roomTypeId provided room type id
     * @return boolean
     */
    public boolean roomTypeExistsById(Long roomTypeId) {
        return roomTypeRepository.existsById(roomTypeId);
    }

    /**
     * @param roomTypeLabel provided room type label
     * @return boolean
     */
    public boolean roomTypeExistsByLabel(String roomTypeLabel) {
        return roomTypeRepository.existsByLabel(roomTypeLabel);
    }

    /**
     * @param roomType provided roomType
     * @return boolean
     */
    public boolean roomExistsByRoomType(RoomType roomType) {
        return roomRepository.existsByRoomType(roomType);
    }

    /**
     * @param roomTypeLabel provided room type label
     * @param roomTypeId    provided room type id
     * @return boolean
     */
    public boolean roomTypeExistsByRoomTypeLabelAndRoomTypeIdIsNot
    (String roomTypeLabel, Long roomTypeId) {
        return roomTypeRepository.existsByLabelAndIdIsNot(roomTypeLabel, roomTypeId);
    }

}
