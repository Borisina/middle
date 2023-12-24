package com.kolya.middle.config;

import com.kolya.middle.model.Weather;
import com.kolya.middle.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    private WeatherService weatherService;

    @Scheduled(cron = "*/5 * * * * *") // this will run the task every day at 4am
    public void scheduleTask(){
        List<Weather> weathers = weatherService.findAll();
        weathers.forEach(w -> {
            if (w.getZipCode()!=null && !Objects.equals(w.getZipCode(), "")){
                weatherService.fetchWeatherFromApiByZipCode(w.getZipCode(),w.getCountry());
            } else if (w.getCity()!=null && !Objects.equals(w.getCity(), "")){
                weatherService.fetchWeatherFromApiByCity(w.getCity());
            }
        });
    }
}
