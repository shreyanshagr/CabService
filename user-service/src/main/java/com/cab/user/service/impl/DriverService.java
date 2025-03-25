package com.cab.user.service.impl;

import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.auth.payload.AuthDTO.LogInRequestDTO;
import com.cab.user.auth.service.AuthenticationService;
import com.cab.user.auth.service.JWTService;
import com.cab.user.entity.Driver;
import com.cab.user.enums.Role;
import com.cab.user.payload.DriverDTO.DriverSignupDTO;
import com.cab.user.payload.DriverDTO.DriverSignupResponseDTO;
import com.cab.user.repository.DriverRepository;
import com.cab.user.service.IDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DriverService implements IDriverService {

    private final DriverRepository driverRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public DriverService(DriverRepository driverRepository, JWTService jwtService, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.driverRepository = driverRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }


    @Override
    public DriverSignupResponseDTO save(DriverSignupDTO driverSignupDTO) {
        driverSignupDTO.setPassword(passwordEncoder.encode(driverSignupDTO.getPassword()));
        final Driver driver = driverSignupDTO.toEntity();
        driver.setRole(Role.DRIVER);
        driver.setEnabled(true);
        Driver savedDriver = driverRepository.save(driver);
        log.info("Saved driver -> {}", savedDriver);
        DriverSignupResponseDTO responseDTO = savedDriver.toSignUpResponseDTO();
        log.info("Response  -> {}", responseDTO);
        Optional<JwtResponseDTO> jwtResponseDTO = jwtService.generateToken(responseDTO.getEmail(), Role.DRIVER);
        jwtResponseDTO.ifPresent(responseDTO::setJwtResponseDTO);
        return responseDTO;
    }

    @Override
    public Optional<JwtResponseDTO> login(LogInRequestDTO logInRequestDTO) {
        return authenticationService.authenticate(logInRequestDTO);
    }
}
