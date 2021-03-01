package com.hotel.reservation.config.security.authority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum AppUserRole {
    USER(Set.of(AppUserPermission.ROOM_READ, AppUserPermission.ROOM_TYPE_READ)),

    ADMIN(Set.of(AppUserPermission.ORDERS_WRITE, AppUserPermission.ROOM_WRITE,
            AppUserPermission.ROOM_TYPE_WRITE, AppUserPermission.ORDERS_READ,
            AppUserPermission.ROOM_READ, AppUserPermission.ROOM_TYPE_READ));

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
