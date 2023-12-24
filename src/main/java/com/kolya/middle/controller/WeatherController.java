package com.kolya.middle.controller;

import com.kolya.middle.model.Weather;
import com.kolya.middle.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/city/{city}")
    public ResponseEntity<?> getWeatherByCity(@PathVariable String city) {
        city = city.substring(0,1).toUpperCase()+city.substring(1);
        Weather weather =  weatherService.getWeatherByCity(city);
        if(weather != null)
            return ResponseEntity.ok(weather);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/weather/zipcode/{country}/{zipCode}")
    public ResponseEntity<?> getWeatherByZipCode(@PathVariable String zipCode, @PathVariable String country) {
        Weather weather =  weatherService.getWeatherByZipCode(zipCode,country);
        if(weather != null)
            return ResponseEntity.ok(weather);
        else
            return ResponseEntity.notFound().build();
    }

}