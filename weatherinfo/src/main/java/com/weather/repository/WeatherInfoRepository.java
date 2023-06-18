package com.weather.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weather.model.WeatherInfo;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Integer> {
	 
	WeatherInfo findByPincodeAndDate(int pincode, LocalDate date);
	
}