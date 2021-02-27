package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.RoomType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RoomRepository extends CrudRepository<Room, Long> {

    Page<Room> findAll(Pageable pageable);

    boolean existsByRoomType(RoomType roomType);

    Page<Room> findByRoomType(RoomType roomType, Pageable pageable);

    Optional<Room> findByLabel(@Param("Label") String label);

    boolean existsByLabelAndIdIsNot(String label, Long id);

    boolean existsByLabel(@Param("label") String label);

    boolean existsById(@NotNull Long id);

    Optional<Room> findByIdAndRoomType(Long id, RoomType roomType);

}