package com.cab.user.payload;

import com.cab.user.auth.payload.AuthDTO;
import com.cab.user.entity.Driver;
import com.cab.user.enums.Gender;
import com.cab.user.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

public class DriverDTO {

    private DriverDTO(){}

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverSignupDTO{

        @NotBlank(message = "Email cannot be null or empty")
        private String email;

        private String name;

        private String mobileNumber;

        @NotBlank(message = "Password cannot be null or empty")
        private String password;

        private LocalDate birthDate;

        private Gender gender;

        @NotNull
        private String licenseNumber;

        @NotNull
        private String vehicleNumber;

        private String vehicleDescription;

        private boolean isAvailable;

        private double currLongitude;

        private double currLatitude;

        public Driver toEntity() {
            final Driver driver = new Driver();
            BeanUtils.copyProperties(this, driver);
            return driver;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverSignupResponseDTO {

        private Integer userId;

        private String name;

        private String email;

        private String mobileNumber;

        private Role role;

        private LocalDate birthDate;

        private Gender gender;

        private String licenseNumber;

        private String vehicleNumber;

        private String vehicleDescription;

        private boolean isAvailable;

        private double currLongitude;

        private double currLatitude;

        private AuthDTO.JwtResponseDTO jwtResponseDTO;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DriverUpdateDTO {

        private String name;

        private String mobileNumber;

        private String profilePicture;

        private LocalDate birthDate;

        private String vehicleNumber;

        private String vehicleDescription;

        private Gender gender;
    }

}
