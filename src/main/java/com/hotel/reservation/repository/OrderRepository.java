package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Order;
import com.hotel.reservation.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAllByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate);

    boolean existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(
            Room room
            , LocalDate startDate
            , LocalDate endDate
    );

    List<Order> findAllByRoom(Room roomByLabel);
}