package com.hotel.reservation.service;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.exception.room.RoomIdMustBeEqualToPassedID;
import com.hotel.reservation.exception.room.RoomIdMustBeZeroOrNullException;
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

    public Iterable<RoomType> getAllRoomTypes() {
        log.info("in getAllRoomTypes method ");
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(Long roomTypeId) {
        log.debug("Room Type ID is :{}", roomTypeId);
        return roomTypeRepository.findById(roomTypeId).orElseThrow(RoomTypeNotFoundException::new);
    }

    public RoomType createRoomType(RoomType roomType) {

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

    public RoomType deleteRoomTypeById(Long id) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(RoomTypeNotFoundException::new);
        boolean roomExistsByRoomType = roomRepository.existsByRoomType(roomType);

        log.debug("Room exists by Room Type :{}", roomExistsByRoomType);
        if (!roomExistsByRoomType) {
            log.error("Room Type is in Use");
            throw new RoomTypeIsUsedException();
        }

        log.info("Room id deleted :{}", id);

        roomTypeRepository.deleteById(id);
        return roomType;
    }

    public RoomType updateRoomTypeById(Long roomTypeId, RoomType roomType) {
        getRoomTypeById(roomTypeId);
        Optional<RoomType> byLabel = roomTypeRepository.findByLabel(roomType.getLabel());
        roomType.setId(roomTypeId);

        if (!roomType.getId().equals(roomTypeId)) {
            log.error("Passed Id is not equal to ROOM TYPE ID");
            throw new RoomIdMustBeEqualToPassedID();
        }

        if (byLabel.isEmpty() || (byLabel.get().getId().equals(roomTypeId))) {

            log.info("Room Type updated :{}", roomType);

            return roomTypeRepository.save(roomType);
        } else {

            log.error("Room Type Label Already Exists");
            throw new RoomTypeLabelAlreadyExistsException();
        }

    }

    public Room createRoomByRoomType(Long roomTypeId, Room room) {

        if (Objects.nonNull(room.getId()) && 0L != room.getId()) {
            log.error("ROOM ID must be zero or null");
            throw new RoomIdMustBeZeroOrNullException();
        }

        RoomType roomType = getRoomTypeById(roomTypeId);
        log.info("Room type is :{}", roomType);
        room.setRoomType(roomType);
        return roomService.saveRoom(room);
    }

    public List<Room> getAllRoomsByRoomTypeId(Long roomTypeId) {
        RoomType roomType = getRoomTypeById(roomTypeId);
        return roomRepository.findByRoomType(roomType);
    }

}
