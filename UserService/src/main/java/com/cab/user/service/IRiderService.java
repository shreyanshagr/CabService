package com.cab.user.service;

import com.cab.user.payload.RiderDTO.RiderSignupDTO;
import com.cab.user.payload.RiderDTO.RiderSignupResponseDTO;
import com.cab.user.payload.RiderDTO.RiderUpdateDTO;


public interface IRiderService {

    RiderSignupResponseDTO save(RiderSignupDTO riderSignupDTO);

    void updateProfile(Integer riderId, RiderUpdateDTO riderUpdateDTO);
//    void updateDropLocation(Integer riderId, double dropLatitude, double dropLongitude);
    void setEnabled(Integer riderId, boolean enabled);
    boolean isRiderEnabled(Integer riderId);

}
