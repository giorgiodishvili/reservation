package com.hotel.reservation.service;

import org.junit.jupiter.api.Test;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoomTypeServiceTest {
    @Mock
    private RoomTypeRepository roomTypeRepository;
    @Mock
    private RoomRepository RoomRepository;

    private RoomTypeService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new RoomTypeService(roomTypeRepository, RoomRepository);
    }

    @Test
    void itShouldChargeCardSuccessfully() {


    }

}
