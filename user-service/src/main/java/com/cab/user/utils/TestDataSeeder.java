package com.cab.user.utils;

import com.cab.user.entity.Driver;
import com.cab.user.entity.Rider;
import com.cab.user.enums.Gender;
import com.cab.user.enums.Role;
import com.cab.user.repository.DriverRepository;
import com.cab.user.repository.RiderRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class TestDataSeeder implements CommandLineRunner {

    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public TestDataSeeder(RiderRepository riderRepository, DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.riderRepository = riderRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createTestRiders(100);   // Create 10 Riders
        createTestDrivers(20);   // Create 5 Drivers
    }

    private void createTestRiders(int count) {
        for (int i = 0; i < count; i++) {
            Rider rider = Rider.builder().build();
            rider.setName(faker.name().fullName());
            rider.setEmail(faker.internet().emailAddress());
            rider.setMobileNumber(faker.phoneNumber().cellPhone());
            rider.setPassword(passwordEncoder.encode("Test@123"));  // Default test password
            rider.setEnabled(true);
            rider.setRole(Role.RIDER);
            rider.setGender(random.nextBoolean() ? Gender.MALE : Gender.FEMALE);

            // Assign a random Lat-Long coordinate
            rider.setCurrLongitude(getRandomLatitude());
            rider.setCurrLatitude(getRandomLongitude());
            rider.setDropLatitude(getRandomLatitude());
            rider.setDropLongitude(getRandomLongitude());

            riderRepository.save(rider);
            System.out.println("Created Test Rider: " + rider.getEmail());
        }
    }

    private void createTestDrivers(int count) {
        for (int i = 0; i < count; i++) {
            Driver driver = new Driver();
            driver.setName(faker.name().fullName());
            driver.setEmail(faker.internet().emailAddress());
            driver.setMobileNumber(faker.phoneNumber().cellPhone());
            driver.setPassword(passwordEncoder.encode("Test@123"));
            driver.setEnabled(true);
            driver.setRole(Role.DRIVER);
            driver.setGender(random.nextBoolean() ? Gender.MALE : Gender.FEMALE);
            driver.setLicenseNumber("DL" + (100000 + random.nextInt(900000))); // Fake License
            driver.setVehicleNumber("UP-" + (10 + random.nextInt(90)) + "-" + (1000 + random.nextInt(9000)));
            driver.setVehicleDescription("Sedan - " + faker.color().name());
            driver.setAvailable(random.nextBoolean());

            // Assign a random Lat-Long coordinate
            driver.setCurrLongitude(getRandomLongitude());
            driver.setCurrLatitude(getRandomLatitude());
//            System.out.println("Driver " + driver.getEmail() + " Location: (" + latitude + ", " + longitude + ")");

            driverRepository.save(driver);
        }
    }

    private double getRandomLatitude() {
        // Example: Generating latitudes between 28.40 to 28.90 (Delhi region)
        return 28.40 + (random.nextDouble() * (28.90 - 28.40));
    }

    private double getRandomLongitude() {
        // Example: Generating longitudes between 77.00 to 77.50 (Delhi region)
        return 77.00 + (random.nextDouble() * (77.50 - 77.00));
    }
}