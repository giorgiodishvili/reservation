package com.hotel.reservation.repository;

import com.hotel.reservation.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel = "roomCategory", path = "room-category")
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    Optional<RoomType> findByLabel(@Param("label") String label);

    Optional<RoomType> findByLabelOrId(String label, Long id);

}
