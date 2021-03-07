package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RoomRepository extends CrudRepository<Room, Long> {

    List<Room> findAll();

    boolean existsByRoomType(RoomType roomType);

    List<Room> findByRoomType(RoomType roomType);

    Optional<Room> findByLabel(@Param("Label") String label);

    boolean existsByLabelAndIdIsNot(String label, Long id);

    boolean existsByLabel(@Param("label") String label);

    boolean existsById(@NotNull Long id);

    Optional<Room> findByIdAndRoomType(Long id, RoomType roomType);

}