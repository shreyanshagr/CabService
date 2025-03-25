package com.cab.nearbydrivers.services;

import com.cab.nearbydrivers.entity.Driver;

import java.util.List;

public interface IndexingFunctions {
    long latLngToCell(double lat, double lng, int res);
    String latLngToCellAddress(double lat, double lng, int res);
    List<Driver> findNearbyDrivers(double lat, double lng, int radius);
    public List<Long> gridDisk(long h3Index, int k);
}
