package com.weather.service;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weather.model.Location;
import com.weather.model.WeatherInfo;
import com.weather.repository.LocationRepository;
import com.weather.repository.WeatherInfoRepository;

@Service
public class WeatherInfoService {
	@Autowired
	private WeatherInfoRepository weatherInfoRepository;
	@Autowired
	private LocationRepository locationRepository;
	private RestTemplate restTemplate = new RestTemplate();
	private final String openWeatherMapApiKey = "7660be4###################";

	public WeatherInfo saveWeather(WeatherInfo weatherInfo) {
		return weatherInfoRepository.save(weatherInfo);
	}

	public WeatherInfo getWeather(int pincode, LocalDate date) {
		WeatherInfo weatherInfo = weatherInfoRepository.findByPincodeAndDate(pincode, date);
		if (weatherInfo != null) {
			return weatherInfo;
		}

		Location location = locationRepository.findById(pincode).orElse(null);
		if (location == null) {
			location = fetchAndSaveLocation(pincode);
		}
		// System.out.println("not null"+location.toString());
		WeatherInfo weatherInfo1 = fetchWeatherInfo(location.getLatitude(), location.getLongitude(), date, pincode);
		// System.out.println(weatherInfo1.toString());
		if (weatherInfo1 != null) {
			saveWeather(weatherInfo1);
		}

		return weatherInfo1;
	}

	public Location fetchAndSaveLocation(int pincode) {
		String geocodingApiUrl = "https://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",in&appid="
				+ openWeatherMapApiKey;
		ResponseEntity<String> response = restTemplate.getForEntity(geocodingApiUrl, String.class);
		// System.out.println("first method"+response);
		if (response.getStatusCode() == HttpStatus.OK) {
			JSONObject responseJson = new JSONObject(response.getBody());
			// System.out.println(responseJson);
			Double latitude = responseJson.getDouble("lat");
			Double longitude = responseJson.getDouble("lon");

			Location location = new Location();
			location.setPincode(pincode);
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			locationRepository.save(location);
			// System.out.println("returned");
			return location;
		}
		return null;
	}

	private WeatherInfo fetchWeatherInfo(Double latitude, Double longitude, LocalDate date, int pincode) {
		String weatherApiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude
				+ "&appid=" + openWeatherMapApiKey;
		ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl, String.class);
		String responseBody = response.getBody();

		if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
			JSONObject json = new JSONObject(responseBody);

			JSONArray weatherArray = json.getJSONArray("weather");
			JSONObject weatherObject = weatherArray.getJSONObject(0);
			String description = weatherObject.getString("description");

			JSONObject mainObject = json.getJSONObject("main");
			Double temperature = mainObject.getDouble("temp");
			Integer humidity = mainObject.getInt("humidity");

			WeatherInfo weatherInfo = new WeatherInfo();
			weatherInfo.setPincode(pincode);
			weatherInfo.setDate(date);
			weatherInfo.setTemperature(temperature);
			weatherInfo.setHumidity(humidity);
			weatherInfo.setDescription(description);
			return weatherInfo;
		}

		return null;
	}

}
