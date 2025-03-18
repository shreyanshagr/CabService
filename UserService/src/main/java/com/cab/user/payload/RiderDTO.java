package com.cab.user.payload;

import com.cab.user.entity.Rider;
import com.cab.user.enums.Gender;
import com.cab.user.enums.Role;
import com.cab.user.auth.payload.AuthDTO.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;


public class RiderDTO {

    private RiderDTO(){}

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiderSignupDTO{

        @NotBlank(message = "Email cannot be null or empty")
        private String email;

        private String name;

        private String mobileNumber;

        @NotBlank(message = "Password cannot be null or empty")
        private String password;

        private LocalDate birthDate;

        private Gender gender;

        private String pickUpLongitude;

        private String pickUpLatitude;

        private String dropLongitude;

        private String dropLatitude;


        public Rider toEntity() {
            final Rider rider = new Rider();
            BeanUtils.copyProperties(this, rider);
            return rider;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiderSignupResponseDTO {

        private Integer userId;

        private String name;

        private String email;

        private String mobileNumber;

        private Role role;

        private LocalDate birthDate;

        private String pickUpLongitude;

        private String pickUpLatitude;

        private String dropLongitude;

        private String dropLatitude;

        private Gender gender;

        private JwtResponseDTO jwtResponseDTO;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RiderUpdateDTO {

        private String name;

        private String mobileNumber;

        private String profilePicture;

        private LocalDate birthDate;

        private Gender gender;

    }




}
