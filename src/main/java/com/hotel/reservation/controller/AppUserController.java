package com.hotel.reservation.controller;

import com.hotel.reservation.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Users")
@AllArgsConstructor
@Validated
public class AppUserController {


    private AppUserService appUserService;


}
