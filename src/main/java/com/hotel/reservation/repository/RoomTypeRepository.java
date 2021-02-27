package com.hotel.reservation.repository;

import com.hotel.reservation.entity.RoomType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomTypeRepository extends CrudRepository<RoomType, Long> {

    Page<RoomType> findAll(Pageable pageable);

    boolean existsByLabel(@Param("label") String label);

    boolean existsById(@Param("roomTypeId") @NotNull Long roomTypeId);

    boolean existsByLabelAndIdIsNot(String label, Long id);


}
