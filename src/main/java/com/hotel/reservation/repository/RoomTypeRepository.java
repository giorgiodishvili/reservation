package com.hotel.reservation.repository;

import com.hotel.reservation.entity.RoomType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomTypeRepository extends CrudRepository<RoomType, Long> {

    boolean existsByLabel(@Param("label") String label);

    boolean existsById(@Param("roomTypeId") @NotNull Long roomTypeId);

    boolean existsByLabelOrId(String label, Long id);

    boolean existsByLabelAndIdIsNot(String label, Long id);


}
