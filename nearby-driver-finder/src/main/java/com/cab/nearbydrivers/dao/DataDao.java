package com.cab.nearbydrivers.dao;

import com.cab.nearbydrivers.entity.Driver;
import com.cab.nearbydrivers.entity.Rider;
import com.uber.h3core.H3Core;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.cab.nearbydrivers.constants.AppConstants.INITIAL_RESOLUTION;

@Slf4j
@Component
@Data
@AllArgsConstructor
@Builder
public class DataDao {

    private final RestTemplate restTemplate;

    @Autowired
    private H3Core h3;

    public static List<Driver> allDriversList;
    public static List<Rider> allRiderList;
    public static ConcurrentHashMap<Long, Set<Driver>> driverIndex = new ConcurrentHashMap<>();

    @Autowired
    public DataDao(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void populateAllDrivers() {
        ResponseEntity<Driver[]> response = restTemplate.getForEntity(
                "http://localhost:8080/driver/find-all", Driver[].class);
        allDriversList = List.of(response.getBody());
        log.info("All drivers found {}", allDriversList);
    }

    public void populateAllRiders() {
        ResponseEntity<Rider[]> response = restTemplate.getForEntity(
                "http://localhost:8080/rider/find-all", Rider[].class);
        allRiderList = List.of(response.getBody());
        log.info("All riders found {}", allRiderList);
    }

    public Rider findRiderById(Integer riderId) {
        return allRiderList.stream()
                .filter(rider -> rider.getRiderId().equals(riderId))
                .findFirst()
                .orElse(null);
    }

    public Rider getRiderById(int riderId) {
        ResponseEntity<Rider> response = restTemplate.getForEntity(
                "http://localhost:8080/rider/find-by-id/" + riderId, Rider.class);
        return response.getBody();
    }

    public void updateDriverLocation() {
        for (Driver driver : allDriversList) {
            double lat = driver.getCurrLatitude();
            double lng = driver.getCurrLongitude();
            Long newCell = h3.latLngToCell(lat, lng, INITIAL_RESOLUTION);
            Long oldCell = driver.getCurrentCell();
            if (oldCell == null) {
                oldCell = 0L; // Default value to avoid null issues
            }

            if (!Objects.equals(oldCell, newCell)) {
                driverIndex.putIfAbsent(newCell, ConcurrentHashMap.newKeySet());
                driverIndex.get(newCell).add(driver);

                if (oldCell != 0 && driverIndex.containsKey(oldCell)) {
                    driverIndex.get(oldCell).remove(driver);
                }
                driver.setCurrentCell(newCell);
            }
        }
    }
}
