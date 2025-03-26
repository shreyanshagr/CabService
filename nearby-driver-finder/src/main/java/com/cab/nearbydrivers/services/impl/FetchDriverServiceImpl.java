package com.cab.nearbydrivers.services.impl;

import com.cab.nearbydrivers.constants.AppConstants;
import com.cab.nearbydrivers.entity.Driver;
import com.cab.nearbydrivers.services.FetchDriverService;
import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.cab.nearbydrivers.constants.AppConstants.MAX_SEARCH_RADIUS_KM;
import static com.cab.nearbydrivers.dao.DataDao.driverIndex;

@Service
@Slf4j
public class FetchDriverServiceImpl implements FetchDriverService {


    @Autowired
    private IndexingFunctionImpl indexingFunction;

    @Autowired
    private H3Core h3;

//    // Key -> HexIndex, value -> set of Drivers in Hexagon with index as hexIndex
//    private ConcurrentHashMap<Long, Set<Driver>> driverIndex;
//
//    //Create or update map (Key -> HexIndex, value -> set of Drivers in Hexagon with index as hexIndex)
//    public void updateDriverLocation() {
//        List<Driver> driverList = userServiceClient.getAllDrivers();
//        for(Driver driver : driverList){
//            double lat = driver.getCurrLatitude();
//            double lng = driver.getCurrLongitude();
//            long newCell = h3.latLngToCell(lat, lng, INITIAL_RESOLUTION);
//            long oldCell = driver.getCurrentCell();
//
//            // If the driver has moved to a new H3 cell
//            if (oldCell != newCell) {
//                // Add the driver to the new cell
//                driverIndex.putIfAbsent(newCell, ConcurrentHashMap.newKeySet());
//                driverIndex.get(newCell).add(driver);
//
//                // Remove the driver from the old cell (if applicable)
//                if (oldCell != 0 && driverIndex.containsKey(oldCell)) {
//                    driverIndex.get(oldCell).remove(driver);
//                }
//                // Update driver's current cell
//                driver.setCurrentCell(newCell);
//            }
//        }
//    }



//    @Override
//    public List<Driver> findNearByDriver(Integer riderId) {
//        log.info("Find nearby driver by riderId: {}", riderId);
//        List<Driver> drivers = userServiceClient.getAllDrivers();
//
//        Rider rider = userServiceClient.getRiderById(riderId);
//        log.info("Rider: {}", rider);
//
//        List<Long> driversh3IndexList = drivers.stream()
//                .map(driver-> indexingFunction.latLngToCell(driver.getCurrLatitude(), driver.getCurrLongitude(), 2))
//                .toList();
//        log.info("Driversh3IndexList: {}", driversh3IndexList);
//
//        Long riderh3Index = indexingFunction.latLngToCell(rider.getCurrLatitude(), rider.getCurrLongitude(), 5);
//        log.info("Riderh3Index: {}", riderh3Index);
//
//        List<Long> nearByh3IndexList = indexingFunction.gridDisk(riderh3Index, 1);
//        log.info("NearByh3IndexList: {}", nearByh3IndexList);
//
//        return new ArrayList<>();
//    }

    /**
     * ----------------------------------------------------------------------------
     * ringSize = 0:   [0]        (1 hexagon - just the center)
     * ringSize = 1:   [0][1]     (6 hexagons around center, total 7)
     * ringSize = 2:   [0][1][2]  (12 more hexagons, total 19)
     * ringSize = 3:   [0][1][2][3] (18 more hexagons, total 37)
     * ----------------------------------------------------------------------------
     * For practical use cases:
     * ringSize = 1: ~860m radius (good for very local search)
     * ringSize = 2: ~1.7km radius (neighborhood level)
     * ringSize = 3: ~2.6km radius (extended local area)
     * ringSize = 5: ~4.3km radius (typical ride-sharing search radius)
     * -----------------------------------------------------------------------------
     * Remember:
     * Total hexagons = 3k² + 3k + 1 (where k is ringSize)
     * Each increase in ringSize adds another layer of hexagons
     * Larger ringSizes mean more computational work
     * -----------------------------------------------------------------------------
     **/

    public List<Driver> findNearByDriverByIndex(double riderSrcLat, double riderSrcLong) {
        List<Driver> matchedDrivers = new ArrayList<>();
        int currResolution = AppConstants.INITIAL_RESOLUTION;
        int ringSize = 0;


        while(matchedDrivers.size() < AppConstants.MIN_DRIVERS && ringSize <= calculateMaxRingSize(MAX_SEARCH_RADIUS_KM, currResolution)) {

            List<Driver> driverInArea = new ArrayList<>();

            // Cell index of centre
            Long centreCell = h3.latLngToCell(riderSrcLat, riderSrcLong, currResolution);

            //Find all nearby cellIndex
            List<Long> searchArea = h3.gridDisk(centreCell, ringSize);


            // Iterate over each hex cell in the search area
            for (Long cell : searchArea) {

                // Get the set of drivers in the current cell, defaulting to an empty set if none exist
                Set<Driver> drivers = driverIndex.getOrDefault(cell, Collections.emptySet());

                for (Driver driver : drivers) {
                    if (driver.isAvailable()) {
                        driverInArea.add(driver);
                    }
                }

            }


            //Sort the list according to distance
            driverInArea.sort((d1, d2) -> {
                double distance1 = calculateDistance(riderSrcLat, riderSrcLong, d1.getCurrLatitude(), d1.getCurrLongitude());
                double distance2 = calculateDistance(riderSrcLat, riderSrcLong, d2.getCurrLatitude(), d2.getCurrLongitude());
                return Double.compare(distance1, distance2);
            });


            if (driverInArea.size() > AppConstants.MAX_DRIVERS && currResolution < AppConstants.MAX_RESOLUTION) {
                // Too many drivers - increase resolution for more precise matching
                currResolution++;
                ringSize = 0;
            } else if (driverInArea.isEmpty()) {
                // No drivers found - expand search area
                ringSize++;
                if (ringSize > calculateMaxRingSize(MAX_SEARCH_RADIUS_KM, currResolution)) {
                    // If search area too large, decrease resolution and reset ring size
                    currResolution = Math.max(currResolution - 1, AppConstants.MIN_RESOLUTION);
                    ringSize = 0;
                }
            } else {
                // Found good number of drivers
                matchedDrivers = driverInArea;
                break;
            }
        }
        return matchedDrivers;
    }


     public int calculateMaxRingSize(double radiusKm, int currResolution) {

        // Get the edge length of a single hexagon at given resolution
        double hexSize = h3.edgeLength(currResolution, LengthUnit.km);

        // Calculate how many hexagons we need to cover the desired radius
        return (int) Math.ceil(radiusKm / hexSize);
    }

    /**
     * Calculates the great-circle distance between two points on Earth using Haversine formula.
     * @param lat1 Latitude of point 1 in degrees
     * @param lng1 Longitude of point 1 in degrees
     * @param lat2 Latitude of point 2 in degrees
     * @param lng2 Longitude of point 2 in degrees
     * @return Distance in kilometers
     */
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // Earth's radius in kilometers
        final double R = 6371.0;

        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lng1Rad = Math.toRadians(lng1);
        double lat2Rad = Math.toRadians(lat2);
        double lng2Rad = Math.toRadians(lng2);

        // Differences in coordinates
        double dLat = lat2Rad - lat1Rad;
        double dLng = lng2Rad - lng1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate distance
        return R * c;
    }


}
