package com.cab.user.service.impl;


import com.cab.user.entity.Driver;
import com.cab.user.enums.Role;
import com.cab.user.payload.DriverDTO.DriverSignupDTO;
import com.cab.user.payload.DriverDTO.DriverSignupResponseDTO;
import com.cab.user.payload.DriverDTO.DriverUpdateDTO;
import com.cab.user.repo.DriverRepository;

import com.cab.user.service.IDriverService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class DriverService implements IDriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }


    @Override
    public DriverSignupResponseDTO save(DriverSignupDTO driverSignupDTO) {
        driverSignupDTO.setPassword(driverSignupDTO.getPassword());
        final Driver driver = driverSignupDTO.toEntity();
        driver.setRole(Role.DRIVER);
        driver.setEnabled(true);
        Driver savedDriver = driverRepository.save(driver);
        log.info("Saved driver -> {}", savedDriver);
        DriverSignupResponseDTO responseDTO = savedDriver.toSignUpResponseDTO();
        log.info("Response  -> {}", responseDTO);
//        Optional<JwtResponseDTO> jwtResponseDTO = jwtService.generateToken(responseDTO.getEmail(), Role.DRIVER);
//        jwtResponseDTO.ifPresent(responseDTO::setJwtResponseDTO);
        return responseDTO;
    }

    @Override
    public void updateProfile(Integer driverId, DriverUpdateDTO driverUpdateDTO) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        if (driverUpdateDTO.getName() != null) driver.setName(driverUpdateDTO.getName());
        if (driverUpdateDTO.getMobileNumber() != null) driver.setMobileNumber(driverUpdateDTO.getMobileNumber());
        if (driverUpdateDTO.getProfilePicture() != null) driver.setProfilePicture(driverUpdateDTO.getProfilePicture());
        if (driverUpdateDTO.getBirthDate() != null) driver.setBirthDate(driverUpdateDTO.getBirthDate());
        if (driverUpdateDTO.getVehicleNumber() != null) driver.setVehicleNumber(driverUpdateDTO.getVehicleNumber());
        if (driverUpdateDTO.getVehicleDescription() != null) driver.setVehicleDescription(driverUpdateDTO.getVehicleDescription());
        if (driverUpdateDTO.getGender() != null) driver.setGender(driverUpdateDTO.getGender());
        driverRepository.save(driver);
    }

//    @Override
//    public void updateLocation(Integer driverId, double latitude, double longitude) {
//        Driver driver = driverRepository.findById(driverId)
//                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
//        driver.setCurrLatitude(latitude);
//        driver.setCurrLongitude(longitude);
//        driverRepository.save(driver);
//    }

//

    @Override
    public void setEnabled(Integer driverId, boolean enabled) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setEnabled(enabled);
        driverRepository.save(driver);

        // update redis too
        String key = "driver:" + driverId + ":enabled";
        redisTemplate.opsForValue().set(key, enabled, Duration.ofHours(2));
    }

//    @Override
//    public Optional<JwtResponseDTO> login(LogInRequestDTO logInRequestDTO) {
//        return authenticationService.authenticate(logInRequestDTO);
//    }

    @Override
    public boolean isDriverEnabled(Integer driverId){
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        String key = "driver:" + driverId + ":enabled";
        Boolean enabled = (Boolean) redisTemplate.opsForValue().get(key);
        if (enabled == null) {
            // Cache miss: load from DB
            enabled = driver.isEnabled();
            // Cache for future reads (set TTL as needed)
            redisTemplate.opsForValue().set(key, enabled, Duration.ofHours(2));
        }
        return enabled;
    }
}
