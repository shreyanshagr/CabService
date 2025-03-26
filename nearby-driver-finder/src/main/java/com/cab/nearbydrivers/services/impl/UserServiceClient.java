//package com.cab.nearbydrivers.services.impl;
//
//import com.cab.nearbydrivers.entity.Driver;
//import com.cab.nearbydrivers.entity.Rider;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//public class UserServiceClient {
//    private final RestTemplate restTemplate;
//
//    public UserServiceClient(RestTemplateBuilder restTemplateBuilder) {
//        this.restTemplate = restTemplateBuilder.build();
//    }
//
//    public List<Rider> getAllRiders() {
//        ResponseEntity<Rider[]> response = restTemplate.getForEntity(
//                "http://localhost:8080/driver/find-all", Rider[].class);
//        return Arrays.asList(response.getBody());
//    }
//
//    public List<Driver> getAllDrivers() {
//        ResponseEntity<Driver[]> response = restTemplate.getForEntity(
//                "http://localhost:8080/rider/find-all", Driver[].class);
//        return Arrays.asList(response.getBody());
//    }
//
//
//    public Rider getRiderById(int riderId) {
//        ResponseEntity<Rider> response = restTemplate.getForEntity(
//                "http://localhost:8080/rider/find-by-id/" + riderId, Rider.class
//        );
//        return response.getBody();
//    }
//}
