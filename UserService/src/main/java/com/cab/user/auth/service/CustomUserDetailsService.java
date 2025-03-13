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

//    private final DriverRepository driverRepository;
//    private final RiderRepository riderRepository;
//
//    public CustomUserDetailsService(DriverRepository driverRepository, RiderRepository riderRepository) {
//        this.driverRepository = driverRepository;
//        this.riderRepository = riderRepository;
//    }

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RiderRepository riderRepository;


    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        Optional<Driver> driver = driverRepository.findByEmail(email);
        if (driver.isPresent()) {
            return new CustomUserPrinciple(driver.get());
        }

        // If not found, try to find a rider with this email
        Optional<Rider> rider = riderRepository.findByEmail(email);
        if (rider.isPresent()) {
            return new CustomUserPrinciple(rider.get());
        }

        // If neither found, throw exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}
