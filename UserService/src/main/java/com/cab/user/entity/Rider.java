package com.cab.user.entity;

import com.cab.user.enums.Gender;
import com.cab.user.enums.Role;
import com.cab.user.payload.RiderDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer riderId;

    private boolean enabled;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @NotNull
    @Column(nullable = false)
    private String password;

    private String profilePicture = "https://res.cloudinary.com/dwgptuzgd/image/upload/v1716280827/paisa_wapis/avatar/thlnk0rgetlygt1b8mzq.jpg";

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreatedDate
    private LocalDateTime signupDate;

    private LocalDateTime lastLoginDate;

//    private double currLatitude; // by location-service
//
//    private double currLongitude; // by location-service
//
//    private double dropLongitude; // by location-service
//
//    private double dropLatitude;  // by location-service

    public RiderDTO.RiderSignupResponseDTO toSignUpResponseDTO() {
        final RiderDTO.RiderSignupResponseDTO riderSignupResponseDTO = new RiderDTO.RiderSignupResponseDTO();
        BeanUtils.copyProperties(this, riderSignupResponseDTO);
        return riderSignupResponseDTO;
    }
}
