package com.cab.user.service.impl;

import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.auth.payload.AuthDTO.LogInRequestDTO;
import com.cab.user.auth.service.AuthenticationService;
import com.cab.user.auth.service.JWTService;
import com.cab.user.entity.Rider;
import com.cab.user.enums.Role;
import com.cab.user.payload.RiderDTO.RiderSignupDTO;
import com.cab.user.payload.RiderDTO.RiderSignupResponseDTO;
import com.cab.user.repository.RiderRepository;
import com.cab.user.service.IRiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RiderService implements IRiderService {

    private final RiderRepository riderRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public RiderService(RiderRepository riderRepository, JWTService jwtService, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.riderRepository = riderRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }


    @Override
    public RiderSignupResponseDTO save(RiderSignupDTO riderSignupDTO) {
        riderSignupDTO.setPassword(passwordEncoder.encode(riderSignupDTO.getPassword()));
        final Rider rider = riderSignupDTO.toEntity();
        rider.setRole(Role.RIDER);
        rider.setEnabled(true);
        Rider savedrider = riderRepository.save(rider);
        log.info("Saved rider -> {}", savedrider);
        RiderSignupResponseDTO responseDTO = savedrider.toSignUpResponseDTO();
        log.info("Response  -> {}", responseDTO);
        Optional<JwtResponseDTO> jwtResponseDTO = jwtService.generateToken(responseDTO.getEmail(), Role.RIDER);
        jwtResponseDTO.ifPresent(responseDTO::setJwtResponseDTO);
        return responseDTO;
    }

    @Override
    public Optional<JwtResponseDTO> login(LogInRequestDTO logInRequestDTO) {
        return authenticationService.authenticate(logInRequestDTO);
    }
}
