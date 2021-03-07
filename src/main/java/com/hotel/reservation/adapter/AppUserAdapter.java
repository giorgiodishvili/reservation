package com.hotel.reservation.adapter;

import com.hotel.reservation.config.security.authority.AppUserRole;
import com.hotel.reservation.entity.AppUser;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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

    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true)
    public Long getId(){
        return appUser.getId();
    }

    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true)
    public String getUsername(){
        return appUser.getUsername();
    }

    public String getFirstName(){
        return appUser.getFirstName();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return appUser.getAuthorities();
    }

    public String getLastName(){
        return appUser.getLastName();
    }

    public boolean isAccountNonExpired(){
        return appUser.isAccountNonExpired();
    }
    public boolean isEnabled(){
        return appUser.isEnabled();
    }
    /*
    Setters
     */
    public void setFirstName(String firstName){
        appUser.setFirstName(firstName);
    }

    public void setLastName(String lastName){
    appUser.setLastName(lastName);
    }

}
