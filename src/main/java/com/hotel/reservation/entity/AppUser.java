package com.hotel.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.reservation.config.security.authority.AppUserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true)
    private Long id;

    @NotNull(message = "firstName mustn't be null")
    @NotEmpty(message = "firstName mustn't be empty")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull(message = "lastName mustn't be null")
    @NotEmpty(message = "lastName mustn't be empty")
    private String lastName;

    @NotNull(message = "email mustn't be null")
    @Column(name = "email", nullable = false, unique = true,updatable = false)
    @NotEmpty(message = "email mustn't be empty")
    @Email
    private String email;

    @NotNull(message = "password mustn't be null")
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "password mustn't be empty")
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Order> orders;

    public AppUser(String firstName, String lastName, String email, String password, AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return appUserRole.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
