package com.cab.user.auth.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {

    private AuthDTO() {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JwtResponseDTO {

        private String accessToken;

        private String email;

        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LogInRequestDTO {

        @NotBlank(message = "Email cannot be null or empty")
        private String email;

        @NotBlank(message = "Password cannot be null or empty")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenResponse {

        private String accessToken;

        private String email;

    }

}
