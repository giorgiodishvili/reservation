package com.hotel.reservation.service;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import com.hotel.reservation.exception.type.RoomTypeIsUsedException;
import com.hotel.reservation.exception.type.RoomTypeLabelAlreadyExistsException;
import com.hotel.reservation.exception.type.RoomTypeNotFoundException;
import com.hotel.reservation.repository.OrderRepository;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;


    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, OrderRepository orderRepository, RoomRepository roomRepository, RoomService roomService) {
        this.roomTypeRepository = roomTypeRepository;
        this.orderRepository = orderRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    public Iterable<RoomType> getRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id).orElseThrow(RoomTypeNotFoundException::new);
    }

    public RoomType saveRoomType(RoomType roomType) {

        Optional<RoomType> byLabel = roomTypeRepository.findByLabel(roomType.getLabel());
        if (byLabel.isEmpty()) {
            return roomTypeRepository.save(roomType);
        } else {
            throw new RoomTypeLabelAlreadyExistsException();
        }
    }


    public String deleteRoomTypeById(Long id) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(RoomTypeNotFoundException::new);
        List<Room> allByRoomType = roomRepository.findAllByRoomType(roomType);
        if (allByRoomType.isEmpty()) {
            roomTypeRepository.deleteById(id);
            return "Room Type has been deleted";
        } else {
            throw new RoomTypeIsUsedException();
        }
    }


    public RoomType updateRoomTypeById(Long id, RoomType roomType) {
        roomTypeRepository.findById(id).orElseThrow(RoomTypeNotFoundException::new);
        Optional<RoomType> byLabel = roomTypeRepository.findByLabel(roomType.getLabel());
        roomType.setId(id);
        if (byLabel.isEmpty() || (byLabel.get().getId().equals(id))) {
            return roomTypeRepository.save(roomType);
        } else {
            throw new RoomTypeLabelAlreadyExistsException();
        }
    }

    public void deleteAllRoomTypes() {
        roomTypeRepository.deleteAll();
    }

    public Room saveRoomByType(Long id, Room room) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(RoomTypeNotFoundException::new);
        room.setRoomType(roomType);
        return roomService.saveRoom(room);
    }

    public List<Room> getRoomsByTypeId(Long id) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(RoomTypeNotFoundException::new);
        return roomRepository.findAllByRoomType(roomType);
    }

    //TODO add deleteRoomsByTypeID
    public String deleteRoomsByRoomTypeId(Long id) {
        List<Room> roomsByTypeId = getRoomsByTypeId(id);

        return null;
    }
}
