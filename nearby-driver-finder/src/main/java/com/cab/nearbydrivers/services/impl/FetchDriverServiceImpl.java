package com.cab.nearbydrivers.services.impl;

import com.cab.nearbydrivers.entity.Driver;
import com.cab.nearbydrivers.entity.Rider;
import com.cab.nearbydrivers.services.FetchDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FetchDriverServiceImpl implements FetchDriverService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private IndexingFunctionImpl indexingFunction;


    @Override
    public List<Driver> findNearByDriver(Integer riderId) {
        log.info("Find nearby driver by riderId: {}", riderId);
        List<Driver> drivers = userServiceClient.getAllDrivers();

        Rider rider = userServiceClient.getRiderById(riderId);
        log.info("Rider: {}", rider);

        List<Long> driversh3IndexList = drivers.stream()
                .map(driver-> indexingFunction.latLngToCell(driver.getCurrLatitude(), driver.getCurrLongitude(), 2))
                .toList();
        log.info("Driversh3IndexList: {}", driversh3IndexList);

        Long riderh3Index = indexingFunction.latLngToCell(rider.getCurrLatitude(), rider.getCurrLongitude(), 5);
        log.info("Riderh3Index: {}", riderh3Index);

        List<Long> nearByh3IndexList = indexingFunction.gridDisk(riderh3Index, 1);
        log.info("NearByh3IndexList: {}", nearByh3IndexList);

        return new ArrayList<>();
    }
}
