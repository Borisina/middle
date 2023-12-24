package com.kolya.middle.controller;

import com.kolya.middle.model.Weather;
import com.kolya.middle.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testGetWeatherByCity() throws Exception {
        Weather weather = new Weather();
        weather.setCity("Test City");

        when(weatherService.getWeatherByCity(any())).thenReturn(weather);

        mockMvc.perform(get("/weather/city/{city}", "test city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Test City"));
    }

    @Test
    public void testGetWeatherByCityNotFound() throws Exception {
        when(weatherService.getWeatherByCity(any())).thenReturn(null);

        mockMvc.perform(get("/weather/city/{city}", "unknown city"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetWeatherByZipCode() throws Exception {
        Weather weather = new Weather();
        weather.setZipCode("11111");

        when(weatherService.getWeatherByZipCode(any(), any())).thenReturn(weather);

        mockMvc.perform(get("/weather/zipcode/{country}/{zipCode}", "us", "11111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zipCode").value("11111"));

    }

    @Test
    public void testGetWeatherByZipCodeNotFound() throws Exception {
        when(weatherService.getWeatherByZipCode(any(), any())).thenReturn(null);

        mockMvc.perform(get("/weather/zipcode/{country}/{zipCode}", "us", "unknown"))
                .andExpect(status().isNotFound());
    }
}