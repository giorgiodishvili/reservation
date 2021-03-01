package com.hotel.reservation.config.security.authority;

public enum AppUserPermission {
    ORDERS_READ("orders:read"),
    ORDERS_WRITE("orders:write"),
    ROOM_READ("room:read"),
    ROOM_WRITE("room:write"),
    ROOM_TYPE_READ("roomType:read"),
    ROOM_TYPE_WRITE("roomType:write");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
