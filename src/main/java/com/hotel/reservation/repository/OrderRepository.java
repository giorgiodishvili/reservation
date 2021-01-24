package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends CrudRepository<Orders, Long> {

    List<Orders> findAllByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate);

    boolean existsByRoomAndPeriodEndGreaterThanEqualAndPeriodBeginLessThanEqual(
            Room room
            , LocalDate startDate
            , LocalDate endDate
    );
    void deleteByRoom(@Param("room") Room room);

}