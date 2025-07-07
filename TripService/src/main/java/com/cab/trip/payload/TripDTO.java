package com.cab.trip.payload;

import com.cab.trip.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TripDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TripRequestDTO {
        private Integer riderId;
        private double pickupLatitude;
        private double pickupLongitude;
        private double dropLatitude;
        private double dropLongitude;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TripResponseDTO {
        private Long tripId;
        private Integer riderId;
        private Integer driverId;
        private double pickupLatitude;
        private double pickupLongitude;
        private double dropLatitude;
        private double dropLongitude;
        private TripStatus status;
        private Double fare;
        private LocalDateTime requestedAt;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TripStatusUpdateDTO {
        private TripStatus status;
        private Integer driverId; // for accept/assign
        private Double fare; // for complete
    }
}
