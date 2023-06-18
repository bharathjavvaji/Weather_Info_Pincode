package com.weather.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.weather.model.WeatherInfo;
import com.weather.service.WeatherInfoService;

@RunWith(MockitoJUnitRunner.class)
public class WeatherInfoControllerTest {

	@Mock
	private WeatherInfoService weatherInfoService;

	@InjectMocks
	private WeatherInfoController weatherInfoController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(weatherInfoController).build();
	}

	@Test
	public void testGetWeatherInfo_ReturnsWeatherInfo() throws Exception {
		int pincode = 521333;
		LocalDate date = LocalDate.of(2023, 6, 15);

		WeatherInfo expectedWeatherInfo = new WeatherInfo();
		expectedWeatherInfo.setPincode(pincode);
		expectedWeatherInfo.setDate(date);
		expectedWeatherInfo.setTemperature(312.26);
		expectedWeatherInfo.setHumidity(23);
		expectedWeatherInfo.setDescription("scattered clouds");

		when(weatherInfoService.getWeather(pincode, date)).thenReturn(expectedWeatherInfo);

		// GET request
		mockMvc.perform(
				get("/weatherinfo/getweather").param("pincode", String.valueOf(pincode)).param("date", date.toString()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.pincode").value(pincode))
				.andExpect(jsonPath("$.date[0]").value(date.getYear()))
				.andExpect(jsonPath("$.date[1]").value(date.getMonthValue()))
				.andExpect(jsonPath("$.date[2]").value(date.getDayOfMonth()))
				.andExpect(jsonPath("$.temperature").value(312.26)).andExpect(jsonPath("$.humidity").value(23))
				.andExpect(jsonPath("$.description").value("scattered clouds"));
	}
}
