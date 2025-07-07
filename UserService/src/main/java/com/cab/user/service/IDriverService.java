package com.cab.user.service;

import com.cab.user.payload.DriverDTO.DriverSignupDTO;
import com.cab.user.payload.DriverDTO.DriverSignupResponseDTO;
import com.cab.user.payload.DriverDTO.DriverUpdateDTO;


public interface IDriverService {

    DriverSignupResponseDTO save(DriverSignupDTO driverSignupDTO);
    void updateProfile(Integer driverId, DriverUpdateDTO driverUpdateDTO);
//    void updateLocation(Integer driverId, double latitude, double longitude);
    void setEnabled(Integer driverId, boolean enabled);
    boolean isDriverEnabled(Integer driverId);
}
