package com.cab.user.controller;

import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.auth.payload.AuthDTO.LogInRequestDTO;
import com.cab.user.payload.DriverDTO.DriverSignupDTO;
import com.cab.user.repository.DriverRepository;
import com.cab.user.service.IDriverService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/driver")
public class DriverController {

    private final DriverRepository driverRepository;

    private final IDriverService driverService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody final DriverSignupDTO driverSignupDTO,
                                    final HttpServletResponse response) {
        // Validate user input
        log.info(driverSignupDTO.toString());
        if (driverSignupDTO == null) {
            return ResponseEntity.badRequest().body("Driver details cannot be null");
        }

        // Check if user with the provided email already exists
        if (driverRepository.findByEmail(driverSignupDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Driver with this email already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.save(driverSignupDTO));

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody final LogInRequestDTO loginRequestDTO,
                                                     final HttpServletResponse response) {
        Optional<JwtResponseDTO> jwtResponseDTO = driverService.login(loginRequestDTO);
        if (jwtResponseDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponseDTO.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

    }

}
