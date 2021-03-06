package com.hotel.reservation.config.security.authority;

public enum AppUserPermission {
    ORDERS_READ("orders:read"),
    ORDERS_WRITE("orders:write"),
    ORDERS_UPDATE("orders:update"),
    ORDERS_DELETE("orders:delete"),

    ROOM_READ("room:read"),
    ROOM_WRITE("room:write"),
    ROOM_UPDATE("room:update"),
    ROOM_DELETE("room:delete"),

    ROOM_TYPE_READ("roomType:read"),
    ROOM_TYPE_WRITE("roomType:write"),
    ROOM_TYPE_UPDATE("roomType:update"),
    ROOM_TYPE_DELETE("roomType:delete");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
