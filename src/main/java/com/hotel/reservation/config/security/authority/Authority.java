package com.hotel.reservation.config.security.authority;

public class Authority {
    public class Order {
        public static final String READ = "hasAuthority('orders:read')";
        public static final String WRITE = "hasAuthority('orders:write')";
        public static final String UPDATE = "hasAuthority('orders:write')";
        public static final String DELETE = "hasAuthority('orders:write')";
    }

    public class RoomType {
        public static final String READ = "hasAuthority('roomType:read')";
        public static final String WRITE = "hasAuthority('roomType:write')";
        public static final String UPDATE = "hasAuthority('roomType:write')";
        public static final String DELETE = "hasAuthority('roomType:write')";
    }

    public class Room {
        public static final String READ = "hasAuthority('room:read')";
        public static final String WRITE = "hasAuthority('room:write')";
        public static final String UPDATE = "hasAuthority('room:write')";
        public static final String DELETE = "hasAuthority('room:write')";
    }
}
