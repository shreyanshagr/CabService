package com.cab.user.service;

import com.cab.user.auth.payload.AuthDTO;
import com.cab.user.payload.RiderDTO.*;

import java.util.Optional;

public interface IRiderService {

    RiderSignupResponseDTO save(RiderSignupDTO riderSignupDTO);

    public Optional<AuthDTO.JwtResponseDTO> login(AuthDTO.LogInRequestDTO logInRequestDTO);

}
