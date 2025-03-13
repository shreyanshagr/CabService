package com.cab.user.repository;

import com.cab.user.entity.Rider;
import com.cab.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Integer> {
    Optional<Rider> findByEmail(String email);
    List<Rider> findAll();
}
