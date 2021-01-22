package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Orders;
import com.hotel.reservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends JpaRepository<Orders, Long> {

    //    @Query("select a from Orders a")
//    List<Orders> all();
//
    //if nothing return then time is available
    @Query("from Orders a where a.room = :roomId and  :startDate <= a.periodEnd and :endDate >= a.periodBegin")
    List<Orders> isTimeAvailableByRoomId(@Param("roomId") Room room
            , @Param("startDate") LocalDate startDate
            , @Param("endDate") LocalDate endDate
    );

    //    @Query("from Orders a where a.active = true and a.roomId = :roomId and  :startDate <= a.endDateTime and :endDate >= a.startDateTime")
    List<Orders> findAllByRoomAndPeriodEndGreaterThanEqual(Room room, LocalDate currentDate);

    List<Orders> findAllByRoomInAndPeriodEndGreaterThanEqual(List<Room> room, LocalDate currentDate);

    void deleteByRoom(@Param("room") Room room);
//    //returns rooms which arent available
//    @Query("select a.roomId from Orders a where a.active = true and  :startDate <= a.endDateTime and :endDate >= a.startDateTime")
//    List<Room> roomIdNotAvailable(
//            @Param("startDate") LocalDate startDate
//            , @Param("endDate") LocalDate endDate
//    );
//
//    //finds order by tran id (for future USE)
//    List<Orders> findByTransactionId(@Param("transactionId") String id);
}