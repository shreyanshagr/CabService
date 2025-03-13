package com.cab.user.payload;

import com.cab.user.entity.User;
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

public class UserDTO {

    private UserDTO() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSignUpDTO {

        @NotBlank(message = "Email cannot be null or empty")
        private String email;

        private String name;

        private String mobileNumber;

        @NotBlank(message = "Password cannot be null or empty")
        private String password;

        private LocalDate birthDate;

        private Gender gender;


        public User toEntity() {
            final User user = new User();
            BeanUtils.copyProperties(this, user);
            return user;
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSignupResponseDTO {

        private Integer userId;

        private String name;

        private String email;

        private String mobileNumber;

        private Role role;

        private LocalDate birthDate;

        private Gender gender;

        private JwtResponseDTO jwtResponseDTO;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserUpdateDTO {

        private String name;
        private String mobileNumber;
        private String profilePicture;

        private LocalDate birthDate;

        private Gender gender;

    }

}
