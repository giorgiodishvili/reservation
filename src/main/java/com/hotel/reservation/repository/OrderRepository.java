package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface OrderRepository extends CrudRepository<Order, Long> {
    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate, Pageable pageable);

    boolean existsByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate);

    boolean existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(
            Room room
            , LocalDate startDate
            , LocalDate endDate
    );

    Optional<Order> findByIdAndRoom(Long id, Room room);

    boolean existsById(@NotNull Long id);

    Optional<Order> findByUuid(String uuid);
}