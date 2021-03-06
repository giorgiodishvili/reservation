package com.hotel.reservation.adapter;

import com.hotel.reservation.entity.AppUser;

public class AppUserAdapter {

    private final AppUser appUser;

    public AppUserAdapter(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUserAdapter() {
        this(new AppUser());
    }

    public AppUser toAppUser() {
        return appUser;
    }

    /*
    Getters
     */


    /*
    Setters
     */
}
