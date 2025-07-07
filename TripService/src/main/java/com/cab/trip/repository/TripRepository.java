package com.cab.trip.repository;

import com.cab.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRiderId(Integer riderId);
    List<Trip> findByDriverId(Integer driverId);
}
