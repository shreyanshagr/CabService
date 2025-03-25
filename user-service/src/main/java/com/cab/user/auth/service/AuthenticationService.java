package com.cab.user.auth.service;

import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.auth.payload.AuthDTO.LogInRequestDTO;
import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import com.cab.user.enums.Role;
import com.cab.user.repository.DriverRepository;
import com.cab.user.repository.RiderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;

    public AuthenticationService(RiderRepository riderRepository, DriverRepository driverRepository) {
        this.riderRepository = riderRepository;
        this.driverRepository = driverRepository;
    }

    public Optional<JwtResponseDTO> authenticate(LogInRequestDTO logInRequestDTO) {
        try {
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
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return Optional.empty();
        }
    }

    public String authenticate(String username, String password) {
        // First authenticate the user
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        // If authentication successful, load the user details and generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.generateToken(userDetails);
    }
}