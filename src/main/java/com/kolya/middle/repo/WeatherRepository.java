package com.kolya.middle.repo;

import com.kolya.middle.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Weather findByCity(String city);
    Weather findByZipCode(String zipCode);
}
