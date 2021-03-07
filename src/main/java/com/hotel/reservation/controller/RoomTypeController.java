package com.hotel.reservation.controller;

import com.hotel.reservation.adapter.ListAdapter;
import com.hotel.reservation.adapter.RoomAdapter;
import com.hotel.reservation.adapter.RoomTypeAdapter;
import com.hotel.reservation.config.security.authority.Authority;
import com.hotel.reservation.service.RoomService;
import com.hotel.reservation.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/room-type")
@Validated
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomService roomService;


    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService, RoomService roomService) {
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
    }


    @GetMapping
    public Page<RoomTypeAdapter> getRoomTypes(Pageable pageable) {
        return ListAdapter.createPageFromList(roomTypeService.getAllRoomTypes(), pageable);

    }

    @GetMapping("/{roomTypeId}")
    public RoomTypeAdapter getRoomTypeById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId) {
        return new RoomTypeAdapter(roomTypeService.getRoomTypeById(roomTypeId));
    }

    @GetMapping("/{roomTypeId}/rooms")
    public Page<RoomAdapter> getRoomsByRoomTypeId(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, Pageable pageable) {
        return ListAdapter.createPageFromList(roomTypeService.getAllRoomsByRoomTypeId(roomTypeId), pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authority.RoomType.WRITE)
    public RoomTypeAdapter createRoomType(@RequestBody @Valid RoomTypeAdapter roomTypeAdapter) {
        return roomTypeService.createRoomType(roomTypeAdapter);
    }


    @PostMapping("/{roomTypeId}/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authority.Room.WRITE)
    public RoomAdapter createRoomByRoomType(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @RequestBody @Valid RoomAdapter roomAdapter) {
        return roomService.createRoomByRoomType(roomTypeId, roomAdapter);
    }

    @PutMapping("/{roomTypeId}/rooms/{roomId}")
    @PreAuthorize(Authority.Room.UPDATE)
    public RoomAdapter updateRoom(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @PathVariable("roomId") @Min(1) Long roomId, @RequestBody @Valid RoomAdapter roomAdapter) {
        return roomService.updateRoomByRoomTypeAndRoomId(roomTypeId, roomId, roomAdapter);
    }

    @DeleteMapping("/{roomTypeId}/rooms/{roomId}")
    @PreAuthorize(Authority.Room.DELETE)
    public RoomAdapter deleteRoomById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @PathVariable("roomId") @Min(1) Long roomId) {
        return roomService.deleteRoomById(roomTypeId, roomId);
    }

    @PutMapping("/{roomTypeId}")
    @PreAuthorize(Authority.RoomType.UPDATE)
    public RoomTypeAdapter updateRoomTypeById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId, @RequestBody @Valid RoomTypeAdapter roomTypeAdapter) {
        return roomTypeService.updateRoomTypeById(roomTypeId, roomTypeAdapter);
    }

    @DeleteMapping("/{roomTypeId}")
    @PreAuthorize(Authority.RoomType.DELETE)
    public RoomTypeAdapter deleteRoomTypeById(@PathVariable("roomTypeId") @Min(1) Long roomTypeId) {
        return roomTypeService.deleteRoomTypeById(roomTypeId);
    }

}
