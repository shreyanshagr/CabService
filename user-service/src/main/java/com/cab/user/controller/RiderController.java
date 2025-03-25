package com.cab.user.controller;

import com.cab.user.auth.payload.AuthDTO.*;
import com.cab.user.payload.RiderDTO.*;
import com.cab.user.repository.RiderRepository;
import com.cab.user.service.IRiderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/rider")
@Slf4j
public class RiderController {

    private final IRiderService riderService;
    private final RiderRepository riderRepository;

    public RiderController(IRiderService riderService, RiderRepository riderRepository) {
        this.riderService = riderService;
        this.riderRepository = riderRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody final RiderSignupDTO riderSignupDTO,
                                    final HttpServletResponse response) {
        // Validate user input
        log.info(riderSignupDTO.toString());
        if (riderSignupDTO == null) {
            return ResponseEntity.badRequest().body("Rider details cannot be null");
        }

        // Check if user with the provided email already exists
        if (riderRepository.findByEmail(riderSignupDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Rider with this email already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(riderService.save(riderSignupDTO));

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody final LogInRequestDTO loginRequestDTO,
                                                     final HttpServletResponse response) {
        Optional<JwtResponseDTO> jwtResponseDTO = riderService.login(loginRequestDTO);
        if (jwtResponseDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponseDTO.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

    }

    @PostMapping("/fun")
    public ResponseEntity<?> fun(){
        return ResponseEntity.status(HttpStatus.OK).body("Successfully fun");
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(riderRepository.findAll());
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<?> findByEmail(@RequestParam String email){
        return ResponseEntity.status(HttpStatus.OK).body(riderRepository.findByEmail(email));
    }

    @GetMapping("/find-by-id/{riderId}")
    public ResponseEntity<?> findRiderById(@PathVariable Integer riderId){
        return ResponseEntity.status(HttpStatus.OK).body(riderRepository.findById(riderId));
    }


}
