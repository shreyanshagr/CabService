package com.cab.trip.entity;

import com.cab.trip.enums.TripStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    private Integer riderId;

    private Integer driverId;

    private double pickupLatitude;

    private double pickupLongitude;

    private double dropLatitude;

    private double dropLongitude;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    private Double fare;

    private LocalDateTime requestedAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;
}