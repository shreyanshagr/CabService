package com.cab.user.config;

import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import com.cab.user.repository.DriverRepository;
import com.cab.user.repository.RiderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;

    public AuthenticationSuccessListener(DriverRepository driverRepository, RiderRepository riderRepository) {
        this.driverRepository = driverRepository;
        this.riderRepository = riderRepository;
    }


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Optional<Rider> rider = riderRepository.findByEmail(email);
            Optional<Driver> driver = driverRepository.findByEmail(email);

            if(rider.isPresent()) {
                rider.get().setLastLoginDate(LocalDateTime.now());
                riderRepository.save(rider.get());
            }

            if(driver.isPresent()) {
                driver.get().setLastLoginDate(LocalDateTime.now());
                driverRepository.save(driver.get());
            }

        }
    }
}