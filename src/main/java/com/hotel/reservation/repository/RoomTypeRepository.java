package com.hotel.reservation.repository;

import com.hotel.reservation.entity.RoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomTypeRepository extends CrudRepository<RoomType, Long> {

    Optional<RoomType> findByLabel(@Param("label") String label);

    Optional<RoomType> findByLabelOrId(String label, Long id);

}
