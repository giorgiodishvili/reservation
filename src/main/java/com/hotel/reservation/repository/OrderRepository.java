package com.hotel.reservation.repository;

import com.hotel.reservation.entity.AppUser;
import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();

    List<Order> findAllByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate);

    boolean existsByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate);

    boolean existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(
            Room room
            , LocalDate startDate
            , LocalDate endDate
    );

    Optional<Order> findByIdAndRoom(Long id, Room room);

    boolean existsById(@NotNull Long id);

    Optional<Order> findByUuid(String uuid);

    List<Order> findAllByAppUser(AppUser appUser);

}