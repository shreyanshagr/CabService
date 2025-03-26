package com.cab.nearbydrivers.entity;

import com.cab.nearbydrivers.enums.Gender;
import com.cab.nearbydrivers.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rider {

    private Integer riderId;

    private boolean enabled;

    private String name;

    private String email;

    private String mobileNumber;

    private String password;

    private String profilePicture;

    private LocalDate birthDate;

    private Role role;

    private Gender gender;

    private LocalDateTime signupDate;

    private LocalDateTime lastLoginDate;

    private double currLatitude;

    private double currLongitude;

    private double dropLongitude;

    private double dropLatitude;

    private Long currentCell;
}
