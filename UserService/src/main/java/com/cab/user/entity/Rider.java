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
public class Rider extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer riderId;

//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
//    private User user;

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
