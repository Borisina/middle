package com.kolya.middle.service;

import com.kolya.middle.model.Weather;
import com.kolya.middle.repo.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${app.weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    // integrate OpenWeatherMap api
    private final String apiUrl = "http://api.openweathermap.org/data/2.5/weather?units=metric&";

    public Weather getWeatherByCity(String city) {
        Weather weather = weatherRepository.findByCity(city);
        if (weather == null) {
            weather = fetchWeatherFromApiByCity(city);
        }
        return weather;
    }

    public Weather getWeatherByZipCode(String zipCode, String country) {
        Weather weather = weatherRepository.findByZipCode(zipCode);
        if (weather == null) {
            weather = fetchWeatherFromApiByZipCode(zipCode,country);
        }
        return weather;
    }

    public Weather fetchWeatherFromApiByCity(String city) {
        String url = apiUrl + "q={city}&appid={apiKey}";
        Map<String, String> params = new HashMap<>();
        params.put("city", city);
        params.put("apiKey", apiKey);


        Weather weather =  getWeatherFromApi(url,params);
        if (weather==null){
            return null;
        }
        weather.setCity(city);
        save(weather);
        return weather;
    }

    public Weather fetchWeatherFromApiByZipCode(String zipCode, String country) {
        String url = apiUrl + "zip={zipCode},"+country+"&appid={apiKey}";
        Map<String, String> params = new HashMap<>();
        params.put("zipCode", zipCode);
        params.put("apiKey", apiKey);

        Weather weather =  getWeatherFromApi(url,params);
        if (weather==null){
            return null;
        }
        weather.setZipCode(zipCode);
        save(weather);
        return weather;
    }

    public void save(Weather weather) {
        if(weather.getZipCode()!=null &&!Objects.equals(weather.getZipCode(), "")) {
            Weather w = weatherRepository.findByZipCode(weather.getZipCode());
            if (w != null) {
                weather.setId(w.getId());
            }
        }else if (weather.getCity()!=null&& !Objects.equals(weather.getCity(), "")){
            Weather w = weatherRepository.findByCity(weather.getCity());
            if (w!=null){
                weather.setId(w.getId());
            }
        }
        weatherRepository.save(weather);
    }

    public Weather getWeatherFromApi(String url, Map<String,String> params){
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class, params);
        Map<String, Object> responseMap = response.getBody();

        // assuming the response body contains these fields
        if (responseMap.containsKey("main") && responseMap.containsKey("name")
                && responseMap.containsKey("sys") && responseMap.containsKey("weather")) {

            Map<String, Object> mainMap = (Map<String, Object>) responseMap.get("main");
            Map<String, Object> sysMap =  (Map<String, Object>) responseMap.get("sys");

            String city = (String) responseMap.get("name");
            String country = (String) sysMap.get("country");
            Double temperature = (Double) mainMap.get("temp");
            Integer humidity = (Integer) mainMap.get("humidity");

            List<Map> weatherList =  (List<Map>) responseMap.get("weather");
            String description = (String) weatherList.get(0).get("description");

            Weather weather = new Weather();
            weather.setCity(city);
            weather.setCountry(country);
            weather.setTemperature(temperature);
            weather.setHumidity(humidity);
            weather.setDescription(description);
            System.out.println(weather);
            return weather;
        }
        return null;
    }

    public List<Weather> findAll() {
        return weatherRepository.findAll();
    }
}