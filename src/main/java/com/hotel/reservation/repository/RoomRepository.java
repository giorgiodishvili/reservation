package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RoomRepository extends CrudRepository<Room, Long> {

    List<Room> findAllByRoomType(RoomType roomType);

    Optional<Room> findByLabel(@Param("Label") String label);

    boolean existsByLabelAndIdIsNot(String label, Long id);
}