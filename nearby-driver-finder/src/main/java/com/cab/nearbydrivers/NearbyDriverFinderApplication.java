package com.cab.nearbydrivers;

import com.cab.nearbydrivers.dao.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NearbyDriverFinderApplication implements CommandLineRunner {

	@Autowired
	private DataDao dataDao;

	public static void main(String[] args) {
		SpringApplication.run(NearbyDriverFinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dataDao.populateAllDrivers();
		dataDao.populateAllRiders();
		dataDao.updateDriverLocation();
	}
}
