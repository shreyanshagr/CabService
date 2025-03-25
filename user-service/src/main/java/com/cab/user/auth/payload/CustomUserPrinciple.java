package com.cab.user.auth.payload;

import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import com.cab.user.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserPrinciple implements UserDetails {

    private final String email;
    private final String password;
    private final Role role;
    private final boolean enabled;
    private final Object userEntity; // Can be either Driver or Rider

    public CustomUserPrinciple(Driver driver) {
        this.email = driver.getEmail();
        this.password = driver.getPassword();
        this.role = driver.getRole();
        this.enabled = driver.isEnabled();
        this.userEntity = driver;
    }

    public CustomUserPrinciple(Rider rider) {
        this.email = rider.getEmail();
        this.password = rider.getPassword();
        this.role = rider.getRole();
        this.enabled = rider.isEnabled();
        this.userEntity = rider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Role getRole() {
        return role;
    }

    @SuppressWarnings("unchecked")
    public <T> T getUserEntity() {
        return (T) userEntity;
    }

    public boolean isDriver() {
        return userEntity instanceof Driver;
    }

    public boolean isRider() {
        return userEntity instanceof Rider;
    }

    public Driver getAsDriver() {
        if (!isDriver()) {
            throw new ClassCastException("User is not a Driver");
        }
        return (Driver) userEntity;
    }

    public Rider getAsRider() {
        if (!isRider()) {
            throw new ClassCastException("User is not a Rider");
        }
        return (Rider) userEntity;
    }
}