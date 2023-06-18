package com.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.model.Location;
import com.weather.repository.LocationRepository;

@Service
public class LocationService {

	@Autowired
	private LocationRepository locationRepository;

	public Location savePincodeInfo(Location location) {
		return locationRepository.save(location);
	}

	public Location findByPincode(int pincode) {
		return locationRepository.findById(pincode).orElse(null);
	}
}
