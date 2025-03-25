package com.cab.nearbydrivers.controller;

import com.cab.nearbydrivers.dto.LatLngDTO;
import com.cab.nearbydrivers.services.FetchDriverService;
import com.cab.nearbydrivers.services.IndexingFunctions;
import com.cab.nearbydrivers.services.impl.UserServiceClient;
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
    private IndexingFunctions indexingFunctions;

    @Autowired
    private FetchDriverService fetchDriverService;

    @Autowired
    UserServiceClient userServiceClient;

    @GetMapping("/latLngToCell")
    public ResponseEntity<?> getLatLngToCell(@RequestBody LatLngDTO latLng) {
        return ResponseEntity.ok(indexingFunctions.latLngToCell(latLng.getLat(), latLng.getLng(), latLng.getRes()));
    }

    @GetMapping("/find-all-driver")
    public ResponseEntity<?> findAllDrivers() {
        return ResponseEntity.status(HttpStatus.OK).body(userServiceClient.getAllDrivers());
    }

    //returns the list of divers in the proximity
    @GetMapping("/find-driver/{riderId}")
    public ResponseEntity<?> findDriver(@PathVariable Integer riderId) {
        log.info("Api hit");
        return ResponseEntity.status(HttpStatus.OK).body(fetchDriverService.findNearByDriver(riderId));
    }

    @GetMapping("/fun")
    public ResponseEntity<?> fun() {
        return ResponseEntity.status(HttpStatus.OK).body("hii fucker");
    }

}
