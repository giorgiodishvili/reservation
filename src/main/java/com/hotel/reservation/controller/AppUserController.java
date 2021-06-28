package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.ListAdapter;
import com.hotel.reservation.adapter.OrderAdapter;
import com.hotel.reservation.service.AppUserService;
import com.hotel.reservation.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Users")
@AllArgsConstructor
@Validated
public class AppUserController {


    private final AppUserService appUserService;
    private final OrderService orderService;

    @GetMapping("{userId}")
    public Page<OrderAdapter> findAllOrdersByUserId(@PathVariable("userId") Long userId, Pageable pageable) {
        return ListAdapter.createPageFromList(orderService.findAllOrdersByUser(userId), pageable);
    }
}
