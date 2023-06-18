package com.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.weather.model.Location;
import com.weather.model.WeatherInfo;
import com.weather.repository.LocationRepository;
import com.weather.repository.WeatherInfoRepository;

@RunWith(MockitoJUnitRunner.class)
public class WeatherInfoServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @Mock
    private WeatherInfoRepository weatherInfoRepository;

    @InjectMocks
    private WeatherInfoService weatherInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getWeatherInfo_ValidPincodeAndDate_ReturnsWeatherInfo() {
        int pincode = 521333;
        LocalDate date = LocalDate.of(2023, 6, 15);

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setPincode(pincode);
        weatherInfo.setDate(date);
        weatherInfo.setTemperature(312.26);
        weatherInfo.setHumidity(23);
        weatherInfo.setDescription("scattered clouds");
        when(weatherInfoRepository.findByPincodeAndDate(pincode, date)).thenReturn(weatherInfo);

        WeatherInfo result = weatherInfoService.getWeather(pincode, date);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode());
        assertEquals(date, result.getDate());
        assertEquals(312.26, result.getTemperature());
        assertEquals(23, result.getHumidity());
        assertEquals("scattered clouds", result.getDescription());
    }

    @Test
    public void getWeatherInfo_WeatherInfoNotFound_ReturnsNull() {
        int pincode = 521333;
        LocalDate date = LocalDate.of(2023, 6, 14);
        
        Location location = new Location();
        location.setPincode(pincode);
        location.setLatitude(16.5484);
        location.setLongitude(81.2402);
        when(locationRepository.findById(pincode)).thenReturn(Optional.of(location));

        when(weatherInfoRepository.findByPincodeAndDate(pincode, date)).thenReturn(null);

        Location fetchedLocation = locationRepository.findById(pincode).get();
        
        WeatherInfo fetchedWeatherInfo = weatherInfoRepository.findByPincodeAndDate(pincode, date);

        verify(locationRepository).findById(pincode);
        verify(weatherInfoRepository).findByPincodeAndDate(pincode, date);
        
        assertNotNull(fetchedLocation);
        assertNull(fetchedWeatherInfo);
    }
}
