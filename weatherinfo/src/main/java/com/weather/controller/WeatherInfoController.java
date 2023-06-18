package com.weather.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.weather.model.WeatherInfo;
import com.weather.service.WeatherInfoService;

@RestController
@RequestMapping("/weatherinfo")
public class WeatherInfoController {

	@Autowired
	private WeatherInfoService weatherInfoService;

	@GetMapping("/getweather")
	public ResponseEntity<?> getWeatherInfo(@RequestParam("pincode") int pincode,
			@RequestParam("date") LocalDate date) {
		try {
			WeatherInfo weatherInfo = weatherInfoService.getWeather(pincode, date);
			return ResponseEntity.ok(weatherInfo);
		} catch (HttpClientErrorException ex) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weather information not found");
		}
	}
}
