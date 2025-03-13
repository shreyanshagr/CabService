package com.cab.user.auth.service;

import com.cab.user.auth.payload.AuthDTO.*;
import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import com.cab.user.enums.Role;
import com.cab.user.exceptions.AuthenticationFailedException;
import com.cab.user.repository.DriverRepository;
import com.cab.user.repository.RiderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTService jwtService, RiderRepository riderRepository, DriverRepository driverRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.riderRepository = riderRepository;
        this.driverRepository = driverRepository;
    }

    public Optional<JwtResponseDTO> authenticate(LogInRequestDTO logInRequestDTO) {

            // Create authentication token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    logInRequestDTO.getEmail(),
                    logInRequestDTO.getPassword()
            );

            // Authenticate using AuthenticationManager directly
            Authentication authentication = authenticationManager.authenticate(authToken);

            // If authentication successful, generate token
            if (authentication.isAuthenticated()) {
                // Find user type and generate appropriate token
                Optional<Rider> rider = riderRepository.findByEmail(logInRequestDTO.getEmail());
                if (rider.isPresent()) {
                    return jwtService.generateToken(logInRequestDTO.getEmail(), Role.RIDER);
                }

                Optional<Driver> driver = driverRepository.findByEmail(logInRequestDTO.getEmail());
                if (driver.isPresent()) {
                    return jwtService.generateToken(logInRequestDTO.getEmail(), Role.DRIVER);
                }
            }
            return Optional.empty();
    }
}