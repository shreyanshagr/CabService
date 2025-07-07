package com.cab.user.controller;


import com.cab.user.payload.DriverDTO;
import com.cab.user.payload.DriverDTO.DriverSignupDTO;
import com.cab.user.repo.DriverRepository;
import com.cab.user.service.IDriverService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        // Check if user with the provided mobile number already exists
        if (driverRepository.findByMobileNumber(driverSignupDTO.getMobileNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Driver with this mobile number already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.save(driverSignupDTO));

    }

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody final LogInRequestDTO loginRequestDTO,
//                                                     final HttpServletResponse response) {
//        Optional<JwtResponseDTO> jwtResponseDTO = driverService.login(loginRequestDTO);
//        if (jwtResponseDTO.isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body(jwtResponseDTO.get());
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//    }

    @GetMapping("/find-by-email")
    public ResponseEntity<?> findByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(driverRepository.findByEmail(email));
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(driverRepository.findAll());
    }

    @PutMapping("/update-profile/{driverId}")
    public ResponseEntity<?> updateProfile(@PathVariable Integer driverId, @RequestBody DriverDTO.DriverUpdateDTO driverUpdateDTO) {
        driverService.updateProfile(driverId, driverUpdateDTO);
        return ResponseEntity.ok("Driver profile updated successfully");
    }

    @PutMapping("/update-location/{driverId}")
    public ResponseEntity<?> updateLocation(@PathVariable Integer driverId, @RequestParam double latitude, @RequestParam double longitude) {
        driverService.updateLocation(driverId, latitude, longitude);
        return ResponseEntity.ok("Driver location updated successfully");
    }

    @PutMapping("/set-availability/{driverId}")
    public ResponseEntity<?> setAvailability(@PathVariable Integer driverId, @RequestParam boolean isAvailable) {
        driverService.setAvailability(driverId, isAvailable);
        return ResponseEntity.ok("Driver availability updated successfully");
    }

    @PutMapping("/set-enabled/{driverId}")
    public ResponseEntity<?> setEnabled(@PathVariable Integer driverId, @RequestParam boolean enabled) {
        driverService.setEnabled(driverId, enabled);
        return ResponseEntity.ok("Driver enabled status updated successfully");
    }

}
