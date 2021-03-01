package com.hotel.reservation.controller;

import com.hotel.reservation.registration.RegistrationRequest;
import com.hotel.reservation.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class UserRegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public UserRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }


    @GetMapping(path = "confirm")
    @PreAuthorize("permitAll()")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}

