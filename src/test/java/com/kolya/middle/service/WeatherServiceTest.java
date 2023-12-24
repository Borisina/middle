package com.kolya.middle.service;

import com.kolya.middle.model.Weather;
import com.kolya.middle.repo.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private WeatherRepository weatherRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testGetWeatherByCity() {
        Weather weather = new Weather();
        weather.setCity("Test City");

        Mockito.when(weatherRepository.findByCity(anyString())).thenReturn(weather);

        Weather result = weatherService.getWeatherByCity("Test City");

        assertEquals("Test City", result.getCity());
        Mockito.verify(weatherRepository).findByCity(anyString());
    }

    @Test
    public void testFetchFromApiWhenCityNotFoundInDB() {
        Mockito.when(weatherRepository.findByCity(anyString())).thenReturn(null);

        Weather newWeather = new Weather();
        newWeather.setCity("Test City");

        // Build the map for the expected response from the API
        Map<String, Object> mockApiResponse = new HashMap<>();
        Map<String, Object> mockMain = new HashMap<>();
        Map<String, Object> mockSys = new HashMap<>();
        List<Map<String, Object>> mockWeather = new ArrayList<>();

        mockMain.put("temp", 20.0);
        mockMain.put("humidity", 80);
        mockApiResponse.put("main", mockMain);
        mockApiResponse.put("name", "Test City");
        mockSys.put("country","Test Country");
        mockApiResponse.put("sys", mockSys);
        Map<String, Object> mockMap = new HashMap<>();
        mockMap.put("description","Test description");
        mockWeather.add(mockMap);
        mockApiResponse.put("weather", mockWeather);

        //Wrap response in ResponseEntity
        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(mockApiResponse, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(anyString(), eq(Map.class), anyMap())).thenReturn(mockResponseEntity);
        Mockito.when(weatherRepository.save(any())).thenReturn(newWeather);

        Weather result = weatherService.getWeatherByCity("Test City");

        assertEquals("Test City", result.getCity());
        Mockito.verify(weatherRepository, times(2)).findByCity(anyString());
        Mockito.verify(restTemplate).getForEntity(anyString(), eq(Map.class), anyMap());
        Mockito.verify(weatherRepository).save(any());
    }

    @Test
    public void fetchFromApiWhenCityNotFoundInDBAndApiReturnsNull() {

        Map<String, Object> mockApiResponse = new HashMap<>();
        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(mockApiResponse, HttpStatus.OK);

        Mockito.when(weatherRepository.findByCity(anyString())).thenReturn(null);
        Mockito.when(restTemplate.getForEntity(anyString(), eq(Map.class), anyMap())).thenReturn(mockResponseEntity);

        Weather result = weatherService.getWeatherByCity("Test City");

        assertNull(result);
        Mockito.verify(weatherRepository).findByCity(anyString());
        Mockito.verify(restTemplate).getForEntity(anyString(), eq(Map.class), anyMap());
        Mockito.verify(weatherRepository, never()).save(any());
    }

    @Test
    public void getWeatherByZipCode() {
        Weather weather = new Weather();
        weather.setZipCode("11111");

        Mockito.when(weatherRepository.findByZipCode(anyString())).thenReturn(weather);

        Weather result = weatherService.getWeatherByZipCode("11111", "us");

        assertEquals("11111", result.getZipCode());
        Mockito.verify(weatherRepository).findByZipCode(anyString());
    }

    @Test
    public void fetchFromApiWhenZipNotFoundInDB() {
        Mockito.when(weatherRepository.findByZipCode(anyString())).thenReturn(null);

        Weather newWeather = new Weather();
        newWeather.setZipCode("11111");

        // Build the map for the expected response from the API
        Map<String, Object> mockApiResponse = new HashMap<>();
        Map<String, Object> mockMain = new HashMap<>();
        Map<String, Object> mockSys = new HashMap<>();
        List<Map<String, Object>> mockWeather = new ArrayList<>();

        mockMain.put("temp", 20.0);
        mockMain.put("humidity", 80);
        mockApiResponse.put("main", mockMain);
        mockApiResponse.put("name", "Test City");
        mockSys.put("country","Test Country");
        mockApiResponse.put("sys", mockSys);
        Map<String, Object> mockMap = new HashMap<>();
        mockMap.put("description","Test description");
        mockWeather.add(mockMap);
        mockApiResponse.put("weather", mockWeather);

        //Wrap response in ResponseEntity
        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(mockApiResponse, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(anyString(), eq(Map.class), anyMap())).thenReturn(mockResponseEntity);
        Mockito.when(weatherRepository.save(any())).thenReturn(newWeather);

        Weather result = weatherService.getWeatherByZipCode("11111", "us");

        assertEquals("11111", result.getZipCode());
        Mockito.verify(weatherRepository, times(2)).findByZipCode(anyString());
        Mockito.verify(restTemplate).getForEntity(anyString(), eq(Map.class), anyMap());
        Mockito.verify(weatherRepository).save(any());
    }

    @Test
    public void fetchFromApiWhenZipNotFoundInDBAndApiReturnsNull() {

        Map<String, Object> mockApiResponse = new HashMap<>();

        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(mockApiResponse, HttpStatus.OK);

        Mockito.when(weatherRepository.findByZipCode(anyString())).thenReturn(null);
        Mockito.when(restTemplate.getForEntity(anyString(), eq(Map.class), anyMap())).thenReturn(mockResponseEntity);

        Weather result = weatherService.getWeatherByZipCode("11111", "us");

        assertNull(result);
        Mockito.verify(weatherRepository).findByZipCode(anyString());
        Mockito.verify(restTemplate).getForEntity(anyString(), eq(Map.class), anyMap());
        Mockito.verify(weatherRepository, never()).save(any());
    }

    @Test
    public void saveAndFetchWeather() {

        Map<String, Object> mockApiResponse = new HashMap<>();
        Map<String, Object> mockMain = new HashMap<>();
        Map<String, Object> mockSys = new HashMap<>();
        List<Map<String, Object>> mockWeather = new ArrayList<>();

        mockMain.put("temp", 20.0);
        mockMain.put("humidity", 80);
        mockApiResponse.put("main", mockMain);
        mockApiResponse.put("name", "Test City");
        mockSys.put("country","Test Country");
        mockApiResponse.put("sys", mockSys);
        Map<String, Object> mockMap = new HashMap<>();
        mockMap.put("description","Test description");
        mockWeather.add(mockMap);
        mockApiResponse.put("weather", mockWeather);

        //Wrap response in ResponseEntity
        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(mockApiResponse, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(anyString(), eq(Map.class), anyMap())).thenReturn(mockResponseEntity);

        Weather weather = new Weather();
        weather.setCity("Test City");

        Mockito.when(weatherRepository.save(any())).thenReturn(weather);
        Mockito.when(weatherRepository.findByCity(anyString())).thenReturn(weather);

        Weather savedWeather = weatherService.fetchWeatherFromApiByCity("Test City");
        Weather fetchedWeather = weatherService.getWeatherByCity("Test City");

        assertEquals(savedWeather.getCity(), fetchedWeather.getCity());
    }

    @Test
    public void findAll() {
        List<Weather> weathers = Arrays.asList(new Weather(), new Weather());
        Mockito.when(weatherRepository.findAll()).thenReturn(weathers);

        List<Weather> allWeathers = weatherService.findAll();

        assertEquals(weathers.size(), allWeathers.size());
        Mockito.verify(weatherRepository).findAll();
    }

}