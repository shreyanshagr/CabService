package com.cab.user.auth.service;

import com.cab.user.auth.payload.CustomUserPrinciple;
import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import com.cab.user.repository.DriverRepository;
import com.cab.user.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RiderRepository riderRepository;
    
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find a Rider
        Optional<Rider> rider = riderRepository.findByEmail(username);
        if (rider.isPresent()) {
            return new CustomUserPrinciple(rider.get());
        }

        // If not a rider, try to find a Driver
        Optional<Driver> driver = driverRepository.findByEmail(username);
        if (driver.isPresent()) {
            return new CustomUserPrinciple(driver.get());
        }

        // If neither rider nor driver is found, throw exception
        throw new UsernameNotFoundException("User not found with email: " + username);
    }

}
