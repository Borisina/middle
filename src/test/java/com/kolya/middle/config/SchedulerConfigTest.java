package com.kolya.middle.config;

import com.kolya.middle.model.Weather;
import com.kolya.middle.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(SchedulerConfig.class)
@EnableScheduling
public class SchedulerConfigTest {

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private SchedulerConfig scheduler;


    // This verifies that each found weather object has fetchWeatherFromApiByZipCode or fetchWeatherFromApiByCity called on it
    @Test
    public void testScheduleTask() {
        Weather w1 = new Weather();
        Weather w2 = new Weather();
        w1.setZipCode("111000");
        w2.setCity("Test city");
        List<Weather> weathers = Arrays.asList(w1, w2);
        when(weatherService.findAll()).thenReturn(weathers);
        scheduler.scheduleTask();
        verify(weatherService, times(1)).fetchWeatherFromApiByZipCode(any(),any());
        verify(weatherService, times(1)).fetchWeatherFromApiByCity(any());

    }

    // Testing for the method get called when the if condition for ZipCode is true
    @Test
    public void testScheduleTaskForZipCode() {
        Weather weatherWithZip = new Weather();
        weatherWithZip.setZipCode("11111");
        weatherWithZip.setCountry("RU");

        Mockito.when(weatherService.findAll()).thenReturn(Collections.singletonList(weatherWithZip));

        scheduler.scheduleTask();

        Mockito.verify(weatherService, times(1)).fetchWeatherFromApiByZipCode(anyString(), anyString());
        Mockito.verify(weatherService, times(0)).fetchWeatherFromApiByCity(anyString());
    }

    // Testing for the method get called when the if condition for City is true
    @Test
    public void testScheduleTaskForCity() {
        Weather weatherWithCity = new Weather();
        weatherWithCity.setCity("Test City");

        Mockito.when(weatherService.findAll()).thenReturn(Collections.singletonList(weatherWithCity));

        scheduler.scheduleTask();

        Mockito.verify(weatherService, times(0)).fetchWeatherFromApiByZipCode(anyString(), anyString());
        Mockito.verify(weatherService, times(1)).fetchWeatherFromApiByCity(anyString());
    }

    // Testing for an empty list from the Weather Service
    @Test
    public void testScheduleTaskForEmptyList() {
        Mockito.when(weatherService.findAll()).thenReturn(Collections.EMPTY_LIST);

        scheduler.scheduleTask();
        // Verifying that neither of the fetch methods were called since the list is empty
        Mockito.verify(weatherService, times(0)).fetchWeatherFromApiByZipCode(anyString(), anyString());
        Mockito.verify(weatherService, times(0)).fetchWeatherFromApiByCity(anyString());
    }
}