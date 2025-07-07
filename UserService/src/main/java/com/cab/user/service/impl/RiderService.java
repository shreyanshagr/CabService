package com.cab.user.service.impl;

import com.cab.user.entity.Rider;
import com.cab.user.enums.Role;
import com.cab.user.payload.RiderDTO.RiderSignupDTO;
import com.cab.user.payload.RiderDTO.RiderSignupResponseDTO;
import com.cab.user.payload.RiderDTO.RiderUpdateDTO;
import com.cab.user.repo.RiderRepository;

import com.cab.user.service.IRiderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class RiderService implements IRiderService {

    @Autowired
    private  RiderRepository riderRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RiderSignupResponseDTO save(RiderSignupDTO riderSignupDTO) {
//        riderSignupDTO.setPassword(passwordEncoder.encode(riderSignupDTO.getPassword()));
        riderSignupDTO.setPassword(riderSignupDTO.getPassword());
        final Rider rider = riderSignupDTO.toEntity();
        rider.setRole(Role.RIDER);
        rider.setEnabled(true);
        Rider savedrider = riderRepository.save(rider);
        log.info("Saved rider -> {}", savedrider);
        RiderSignupResponseDTO responseDTO = savedrider.toSignUpResponseDTO();
        log.info("Response  -> {}", responseDTO);
//        Optional<JwtResponseDTO> jwtResponseDTO = jwtService.generateToken(responseDTO.getEmail(), Role.RIDER);
//        jwtResponseDTO.ifPresent(responseDTO::setJwtResponseDTO);
        return responseDTO;
    }

    @Override
    public void updateProfile(Integer riderId, RiderUpdateDTO riderUpdateDTO) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new IllegalArgumentException("Rider not found"));
        if (riderUpdateDTO.getName() != null) rider.setName(riderUpdateDTO.getName());
        if (riderUpdateDTO.getMobileNumber() != null) rider.setMobileNumber(riderUpdateDTO.getMobileNumber());
        if (riderUpdateDTO.getProfilePicture() != null) rider.setProfilePicture(riderUpdateDTO.getProfilePicture());
        if (riderUpdateDTO.getBirthDate() != null) rider.setBirthDate(riderUpdateDTO.getBirthDate());
        if (riderUpdateDTO.getGender() != null) rider.setGender(riderUpdateDTO.getGender());
        riderRepository.save(rider);
    }

//    @Override
//    public void updateDropLocation(Integer riderId, double dropLatitude, double dropLongitude) {
//        Rider rider = riderRepository.findById(riderId)
//                .orElseThrow(() -> new IllegalArgumentException("Rider not found"));
//        rider.setDropLatitude(dropLatitude);
//        rider.setDropLongitude(dropLongitude);
//        riderRepository.save(rider);
//    }

    @Override
    public void setEnabled(Integer riderId, boolean enabled) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new IllegalArgumentException("Rider not found"));
        rider.setEnabled(enabled);
        riderRepository.save(rider);

        // update redis too
        String key = "rider:" + riderId + ":enabled:";
        redisTemplate.opsForValue().set(key, enabled, Duration.ofHours(2));

    }

    @Override
    public boolean isRiderEnabled(Integer riderId){
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new IllegalArgumentException("Rider not found"));

        String key = "rider:" + riderId + ":enabled";
        Boolean enabled = (Boolean) redisTemplate.opsForValue().get(key);
        if (enabled == null) {
            // Cache miss: load from DB
            enabled = rider.isEnabled();
            // Cache for future reads (set TTL as needed)
            redisTemplate.opsForValue().set(key, enabled, Duration.ofHours(2));
        }
        return enabled;
    }

//    @Override
//    public Optional<JwtResponseDTO> login(LogInRequestDTO logInRequestDTO) {
//        return authenticationService.authenticate(logInRequestDTO);
//    }
}
