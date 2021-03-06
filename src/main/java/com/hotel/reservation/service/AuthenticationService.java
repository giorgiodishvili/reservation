package com.hotel.reservation.service;

import com.hotel.reservation.adapter.authentication.AuthRequest;
import com.hotel.reservation.adapter.authentication.AuthResponse;
import com.hotel.reservation.config.security.jwt.JwtTokenProvider;
import com.hotel.reservation.entity.AppUser;
import com.hotel.reservation.exception.security.InvalidEmailOrPasswordException;
import com.hotel.reservation.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            AppUser user = appUserRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email not found exception"));
            String token = jwtTokenProvider.createToken(authRequest.getEmail(), user.getAppUserRole().name());
            return new AuthResponse(token);
        } catch (AuthenticationException e) {
            throw new InvalidEmailOrPasswordException();

        }
    }
}
