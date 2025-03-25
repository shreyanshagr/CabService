package com.cab.nearbydrivers.services;

import com.cab.nearbydrivers.entity.Driver;

import java.util.List;

public interface FetchDriverService {
    List<Driver> findNearByDriver(Integer riderId);
}
