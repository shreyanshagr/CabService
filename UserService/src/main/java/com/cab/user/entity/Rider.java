package com.cab.user.entity;

import com.cab.user.payload.RiderDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "riders")
@PrimaryKeyJoinColumn(name = "user_id")
public class Rider extends User {

    private String pickUpLongitude;
    private String pickUpLatitude;
    private String dropLongitude;
    private String dropLatitude;

    public RiderDTO.RiderSignupResponseDTO toSignUpResponseDTO() {
        final RiderDTO.RiderSignupResponseDTO riderSignupResponseDTO = new RiderDTO.RiderSignupResponseDTO();
        BeanUtils.copyProperties(this, riderSignupResponseDTO);
        return riderSignupResponseDTO;
    }
}
