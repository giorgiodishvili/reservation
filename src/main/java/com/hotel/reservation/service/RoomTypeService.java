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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        log.info("in getAllRoomTypes method ");
        return roomTypeRepository.findAll();
    }

    /**
     * @param roomTypeId provided room type id
     * @return RoomType
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public RoomType getRoomTypeById(Long roomTypeId) {
        log.debug("Room Type ID is :{}", roomTypeId);
        return roomTypeRepository.findById(roomTypeId).orElseThrow(RoomTypeNotFoundException::new);
    }

    /**
     * @param roomType provided room Type
     * @return RoomType
     * @throws RoomTypeIdMustBeZeroOrNullException if room type id is not zero or null
     * @throws RoomTypeLabelAlreadyExistsException if room type label already exists room type cant be added
     */
    //not null anotacia rodis unda gamoviyeno ?
    public RoomType createRoomType(@NotNull RoomType roomType) {

        if (Objects.nonNull(roomType.getId()) && 0L != roomType.getId()) {
            log.error("ROOM TYPE ID must be zero or null");
            throw new RoomTypeIdMustBeZeroOrNullException();
        }

        boolean existsByLabel = roomTypeRepository.existsByLabel(roomType.getLabel());

        log.info("RoomType is :{}", roomType);
        log.debug("Room Type Label Exists :{}", existsByLabel);

        if (existsByLabel) {

            log.error("Room Type Label Already Exists");
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
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(RoomTypeNotFoundException::new);
        boolean roomExistsByRoomType = roomRepository.existsByRoomType(roomType);

        log.debug("Room exists by Room Type :{}", roomExistsByRoomType);
        if (!roomExistsByRoomType) {
            log.error("Room Type is in Use");
            throw new RoomTypeIsUsedException();
        }

        log.info("Room id deleted :{}", roomTypeId);

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
    //not null column-ებზე რომ მიწერია აქაც დაწერა აღარაა ხო აუცილებელი ?
    public RoomType updateRoomTypeById(Long roomTypeId, RoomType roomType) {
        getRoomTypeById(roomTypeId);
        Optional<RoomType> byLabel = roomTypeRepository.findByLabel(roomType.getLabel());
        roomType.setId(roomTypeId);

        if (byLabel.isEmpty() || (byLabel.get().getId().equals(roomTypeId))) {

            log.info("Room Type updated :{}", roomType);

            return roomTypeRepository.save(roomType);
        } else {

            log.error("Room Type Label Already Exists");
            throw new RoomTypeLabelAlreadyExistsException();
        }

    }

    /**
     * @param roomTypeId provided room type if
     * @param room       provided room
     * @return Room
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public Room createRoomByRoomType(Long roomTypeId, @NotNull Room room) {
        RoomType roomType = getRoomTypeById(roomTypeId);
        log.info("Room type is :{}", roomType);
        room.setRoomType(roomType);
        return roomService.createRoom(room);
    }

    /**
     * @param roomTypeId provided room type if
     * @return List of Rooms
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public List<Room> getAllRoomsByRoomTypeId(Long roomTypeId) {
        RoomType roomType = getRoomTypeById(roomTypeId);
        return roomRepository.findByRoomType(roomType);
    }

}
