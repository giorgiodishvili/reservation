package com.hotel.reservation.service;

import com.hotel.reservation.adapter.RoomAdapter;
import com.hotel.reservation.adapter.RoomTypeAdapter;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;


    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, RoomRepository roomRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * @return Iterable of Room Type
     */
    public List<RoomTypeAdapter> getAllRoomTypes() {
        return roomTypeRepository.findAll()
                .stream()
                .map(RoomTypeAdapter::new)
                .collect(Collectors.toList());
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
     * @param roomTypeAdapter provided room Type
     * @return RoomType
     * @throws RoomTypeIdMustBeZeroOrNullException if room type id is not zero or null
     * @throws RoomTypeLabelAlreadyExistsException if room type label already exists room type cant be added
     */
    public RoomTypeAdapter createRoomType(RoomTypeAdapter roomTypeAdapter) {

        if (roomTypeExistsByLabel(roomTypeAdapter.getLabel())) {
            throw new RoomTypeLabelAlreadyExistsException();
        }

        System.out.println("========>" + roomTypeAdapter.getLabel());

        return new RoomTypeAdapter(roomTypeRepository.save(roomTypeAdapter.toRoomType()));
    }

    /**
     * @param roomTypeId provided room type id
     * @return RoomType
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     * @throws RoomTypeIsUsedException   if room type is used
     */
    public RoomTypeAdapter deleteRoomTypeById(Long roomTypeId) {

        RoomType roomType = getRoomTypeById(roomTypeId);

        if (!roomExistsByRoomType(roomType)) {
            throw new RoomTypeIsUsedException();
        }

        roomTypeRepository.deleteById(roomTypeId);

        return new RoomTypeAdapter(roomType);
    }

    /**
     * @param roomTypeId      provided room type id
     * @param roomTypeAdapter provided roomType
     * @return RoomType
     * @throws RoomTypeNotFoundException           if room type is not found by room type id
     * @throws RoomTypeLabelAlreadyExistsException if room type label already exists room type cant be updated
     */
    public RoomTypeAdapter updateRoomTypeById(Long roomTypeId, RoomTypeAdapter roomTypeAdapter) {

        if (!roomTypeExistsById(roomTypeId)) {
            throw new RoomTypeNotFoundException();
        }

        if (roomTypeExistsByRoomTypeLabelAndRoomTypeIdIsNot(roomTypeAdapter.getLabel(), roomTypeId)) {
            throw new RoomTypeLabelAlreadyExistsException();
        }
        RoomType roomType = roomTypeAdapter.toRoomType();
        roomType.setId(roomTypeId);

        return new RoomTypeAdapter(roomTypeRepository.save(roomType));
    }

    /**
     * @param roomTypeId provided room type id
     * @return List of Rooms
     * @throws RoomTypeNotFoundException if room type is not found by room type id
     */
    public List<RoomAdapter> getAllRoomsByRoomTypeId(Long roomTypeId) {
        RoomType roomType = getRoomTypeById(roomTypeId);

        log.debug("Room type is :{}", roomType);
        return roomRepository.findByRoomType(roomType)
                .stream()
                .map(RoomAdapter::new)
                .collect(Collectors.toList());
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
