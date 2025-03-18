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
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Driver extends User {

    @NotNull
    private String licenseNumber;

    @NotNull
    private String vehicleNumber;

    private String vehicleDescription;
    private boolean isAvailable;

    public DriverSignupResponseDTO toSignUpResponseDTO() {
        final DriverSignupResponseDTO driverSignupResponseDTO = new DriverSignupResponseDTO();
        BeanUtils.copyProperties(this, driverSignupResponseDTO);
        return driverSignupResponseDTO;
    }
}
