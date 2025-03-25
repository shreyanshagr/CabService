package com.cab.user.service;

import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.auth.payload.AuthDTO.LogInRequestDTO;
import com.cab.user.payload.DriverDTO.DriverSignupDTO;
import com.cab.user.payload.DriverDTO.DriverSignupResponseDTO;

import java.util.Optional;

public interface IDriverService {

    DriverSignupResponseDTO save(DriverSignupDTO driverSignupDTO);

    public Optional<JwtResponseDTO> login(LogInRequestDTO logInRequestDTO);
}
