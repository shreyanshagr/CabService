package com.cab.nearbydrivers.controller;

import com.cab.nearbydrivers.dao.DataDao;
import com.cab.nearbydrivers.entity.Rider;
import com.cab.nearbydrivers.services.FetchDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cab")
@Slf4j
public class NearbyDriversController {


    @Autowired
    private FetchDriverService fetchDriverService;

    @Autowired
    private DataDao dataDao;

//    @GetMapping("/latLngToCell")
//    public ResponseEntity<?> getLatLngToCell(@RequestBody LatLngDTO latLng) {
//        return ResponseEntity.ok(indexingFunctions.latLngToCell(latLng.getLat(), latLng.getLng(), latLng.getRes()));
//    }
//
//    @GetMapping("/find-all-driver")
//    public ResponseEntity<?> findAllDrivers() {
//        return ResponseEntity.status(HttpStatus.OK).body(userServiceClient.getAllDrivers());
//    }
//
//    //returns the list of divers in the proximity
//    @GetMapping("/find-driver/{riderId}")
//    public ResponseEntity<?> findDriver(@PathVariable Integer riderId) {
//        log.info("Api hit");
//        return ResponseEntity.status(HttpStatus.OK).body(fetchDriverService.findNearByDriver(riderId));
//    }
//
//    @GetMapping("/fun")
//    public ResponseEntity<?> fun() {
//        return ResponseEntity.status(HttpStatus.OK).body("hii fucker");
//    }

    @GetMapping("/find-nearby-driver/{riderId}")
    public ResponseEntity<?> findNearbyDriver(@PathVariable Integer riderId) {
         Rider rider = dataDao.findRiderById(riderId);
        return ResponseEntity.status(HttpStatus.OK).body(fetchDriverService.findNearByDriverByIndex(rider.getDropLatitude(), rider.getDropLongitude()));
    }

}
