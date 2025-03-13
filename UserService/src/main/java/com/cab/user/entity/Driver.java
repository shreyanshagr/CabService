package com.cab.user.entity;

import com.cab.user.payload.DriverDTO.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Driver extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
//    private User user;

    @NotNull
    private String licenseNumber;

    @NotNull
    private String vehicleNumber;

    private String vehicleDescription;

    private boolean isAvailable;

    public DriverSignupResponseDTO toSignUpResponseDTO() {
        final DriverSignupResponseDTO driverSignupResponseDTO = new     DriverSignupResponseDTO();
        BeanUtils.copyProperties(this, driverSignupResponseDTO);
        return driverSignupResponseDTO;
    }


}
