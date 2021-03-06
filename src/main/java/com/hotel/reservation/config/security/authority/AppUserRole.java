package com.hotel.reservation.config.security.authority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum AppUserRole {
    USER(Set.of(AppUserPermission.ROOM_READ, AppUserPermission.ROOM_TYPE_READ, AppUserPermission.ORDERS_READ, AppUserPermission.ORDERS_WRITE, AppUserPermission.ORDERS_UPDATE)),

    ADMIN(Set.of(AppUserPermission.ORDERS_WRITE, AppUserPermission.ORDERS_READ, AppUserPermission.ORDERS_UPDATE, AppUserPermission.ORDERS_DELETE,
            AppUserPermission.ROOM_TYPE_WRITE, AppUserPermission.ROOM_TYPE_READ, AppUserPermission.ROOM_TYPE_UPDATE, AppUserPermission.ROOM_TYPE_DELETE,
            AppUserPermission.ROOM_READ, AppUserPermission.ROOM_WRITE, AppUserPermission.ROOM_UPDATE, AppUserPermission.ROOM_DELETE));

    private final Set<AppUserPermission> permissions;

    AppUserRole(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
                .collect(Collectors.toSet());
    }
}
