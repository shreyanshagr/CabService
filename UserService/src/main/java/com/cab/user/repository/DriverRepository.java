package com.cab.user.repository;

import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByEmail(String email);
    List<Driver> findAll();
    Optional<Driver> findByMobileNumber(String mobileNumber);
}
