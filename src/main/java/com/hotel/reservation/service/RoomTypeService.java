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
        log.trace("executing getAllRoomTypes");
        return roomTypeRepository.findAll();
    }

    /**
     * @param roomTypeId provided room type id
     * @return RoomType
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public RoomType getRoomTypeById(Long roomTypeId) {
        log.trace("executing getRoomTypeById");
        log.debug("Room Type ID is :{}", roomTypeId);
        return roomTypeRepository.findById(roomTypeId).orElseThrow(RoomTypeNotFoundException::new);
    }

    /**
     * @param roomType provided room Type
     * @return RoomType
     * @throws RoomTypeIdMustBeZeroOrNullException if room type id is not zero or null
     * @throws RoomTypeLabelAlreadyExistsException if room type label already exists room type cant be added
     */
    public RoomType createRoomType(RoomType roomType) {
        log.trace("executing createRoomType");

        if (Objects.nonNull(roomType.getId()) && 0L != roomType.getId()) {
            log.info("ROOM TYPE ID must be zero or null exception");
            throw new RoomTypeIdMustBeZeroOrNullException();
        }

        boolean existsByLabel = roomTypeExistsByLabel(roomType.getLabel());

        log.debug("RoomType is :{}", roomType);
        log.debug("Room Type Label Exists: :{}", existsByLabel);

        if (existsByLabel) {
            log.info("Room Type Label Already Exists exception");
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
        log.trace("executing deleteRoomTypeById");

        RoomType roomType = getRoomTypeById(roomTypeId);
        log.debug("RoomType is :{}", roomType);

        boolean roomIsPresent = roomExistsByRoomType(roomType);
        log.debug("Room exists by Room Type :{}", roomIsPresent);

        if (!roomIsPresent) {
            log.error("Room Type is in Use");
            throw new RoomTypeIsUsedException();
        }

        log.info("Id of deleted Room: :{}", roomTypeId);
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
        log.trace("executing updateRoomTypeById");

        if (!roomTypeExistsById(roomTypeId)) {
            throw new RoomTypeNotFoundException();
        }

        roomType.setId(roomTypeId);
        log.debug("RoomType is :{}", roomType);

        if (roomTypeExistsByRoomTypeLabelAndRoomTypeIdIsNot(roomType.getLabel(), roomType.getId())) {

            log.info("Room Type Label Already Exists");
            throw new RoomTypeLabelAlreadyExistsException();

        }
        log.debug("Room Type updated :{}", roomType);
        return roomTypeRepository.save(roomType);
    }

    /**
     * @param roomTypeId provided room type if
     * @param room       provided room
     * @return Room
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public Room createRoomByRoomType(Long roomTypeId, Room room) {
        log.trace("executing createRoomByRoomType");

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
        log.trace("executing getAllRoomsByRoomTypeId");

        RoomType roomType = getRoomTypeById(roomTypeId);

        log.debug("Room type is :{}", roomType);
        return roomRepository.findByRoomType(roomType);
    }

    /**
     * @param roomTypeId provided room type id
     * @return boolean
     */
    public boolean roomTypeExistsById(Long roomTypeId) {
        log.trace("executing getAllRoomsByRoomTypeId");

        log.debug("Room Type id is :{}", roomTypeId);
        return roomTypeRepository.existsById(roomTypeId);
    }

    /**
     * @param roomTypeLabel provided room type label
     * @return boolean
     */
    public boolean roomTypeExistsByLabel(String roomTypeLabel) {
        log.trace("executing roomTypeExistsByLabel");
        log.debug("Room Type label is :{}", roomTypeLabel);

        return roomTypeRepository.existsByLabel(roomTypeLabel);
    }

    /**
     * @param roomType provided roomType
     * @return boolean
     */
    public boolean roomExistsByRoomType(RoomType roomType) {
        log.trace("executing roomExistsByRoomType");
        log.debug("Room Type is :{}", roomType);

        return roomRepository.existsByRoomType(roomType);
    }

    /**
     * @param roomTypeLabel provided room type label
     * @param roomTypeId    provided room type id
     * @return boolean
     */
    public boolean roomTypeExistsByRoomTypeLabelAndRoomTypeIdIsNot(String roomTypeLabel, Long roomTypeId) {
        log.trace("executing roomTypeExistsByRoomTypeLabelAndRoomTypeIdIsNot");
        log.debug("Room Label is :{} and id :{}", roomTypeLabel, roomTypeId);
        return roomTypeRepository.existsByLabelAndIdIsNot(roomTypeLabel, roomTypeId);
    }

}
