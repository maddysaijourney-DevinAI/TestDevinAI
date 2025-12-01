package com.weather.api.service;

import com.weather.api.model.WeatherForecast;
import com.weather.api.model.WeatherRequest;
import com.weather.api.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherRepository repository;

    @InjectMocks
    private WeatherService weatherService;

    private WeatherRequest validRequest;
    private WeatherForecast sampleForecast;

    @BeforeEach
    void setUp() {
        validRequest = new WeatherRequest();
        validRequest.setCity("New York");
        validRequest.setCountry("USA");
        validRequest.setDate(LocalDate.now());
        validRequest.setTemperatureCelsius(20.0);
        validRequest.setCondition("Sunny");
        validRequest.setHumidity(50);
        validRequest.setWindSpeedKmh(10.0);
        validRequest.setWindDirection("N");
        validRequest.setDescription("Test description");

        sampleForecast = new WeatherForecast("New York", "USA", LocalDate.now(), 20.0, "Sunny", 50, 10.0, "N", "Test description");
    }

    @Test
    void createForecast_ShouldCreateAndReturnForecast() {
        when(repository.save(any(WeatherForecast.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WeatherForecast created = weatherService.createForecast(validRequest);

        assertNotNull(created);
        assertEquals("New York", created.getCity());
        assertEquals("USA", created.getCountry());
        assertEquals(20.0, created.getTemperatureCelsius());
        verify(repository, times(1)).save(any(WeatherForecast.class));
    }

    @Test
    void getForecastById_WhenExists_ShouldReturnForecast() {
        when(repository.findById("test-id")).thenReturn(Optional.of(sampleForecast));

        Optional<WeatherForecast> result = weatherService.getForecastById("test-id");

        assertTrue(result.isPresent());
        assertEquals("New York", result.get().getCity());
    }

    @Test
    void getForecastById_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<WeatherForecast> result = weatherService.getForecastById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    void getAllForecasts_ShouldReturnAllForecasts() {
        List<WeatherForecast> forecasts = Arrays.asList(
                sampleForecast,
                new WeatherForecast("London", "UK", LocalDate.now(), 15.0, "Cloudy", 70, 15.0, "W", "Cloudy day")
        );
        when(repository.findAll()).thenReturn(forecasts);

        List<WeatherForecast> result = weatherService.getAllForecasts();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getForecastsByCity_ShouldReturnMatchingForecasts() {
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(repository.findByCity("New York")).thenReturn(forecasts);

        List<WeatherForecast> result = weatherService.getForecastsByCity("New York");

        assertEquals(1, result.size());
        assertEquals("New York", result.get(0).getCity());
    }

    @Test
    void getForecastsByCityAndCountry_ShouldReturnMatchingForecasts() {
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(repository.findByCityAndCountry("New York", "USA")).thenReturn(forecasts);

        List<WeatherForecast> result = weatherService.getForecastsByCityAndCountry("New York", "USA");

        assertEquals(1, result.size());
        assertEquals("USA", result.get(0).getCountry());
    }

    @Test
    void getForecastsByCityAndDate_ShouldReturnMatchingForecasts() {
        LocalDate today = LocalDate.now();
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(repository.findByCityAndDate("New York", today)).thenReturn(forecasts);

        List<WeatherForecast> result = weatherService.getForecastsByCityAndDate("New York", today);

        assertEquals(1, result.size());
    }

    @Test
    void getForecastsByCityAndDateRange_ShouldReturnMatchingForecasts() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(repository.findByCityAndDateRange("New York", startDate, endDate)).thenReturn(forecasts);

        List<WeatherForecast> result = weatherService.getForecastsByCityAndDateRange("New York", startDate, endDate);

        assertEquals(1, result.size());
    }

    @Test
    void updateForecast_WhenExists_ShouldUpdateAndReturnForecast() {
        WeatherForecast existingForecast = new WeatherForecast("Old City", "Old Country", LocalDate.now(), 10.0, "Rainy", 80, 20.0, "S", "Old description");
        when(repository.findById("test-id")).thenReturn(Optional.of(existingForecast));
        when(repository.save(any(WeatherForecast.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<WeatherForecast> result = weatherService.updateForecast("test-id", validRequest);

        assertTrue(result.isPresent());
        assertEquals("New York", result.get().getCity());
        assertEquals("USA", result.get().getCountry());
        assertEquals(20.0, result.get().getTemperatureCelsius());
        assertEquals("Sunny", result.get().getCondition());
        assertEquals(50, result.get().getHumidity());
        assertEquals(10.0, result.get().getWindSpeedKmh());
        assertEquals("N", result.get().getWindDirection());
        assertEquals("Test description", result.get().getDescription());
        verify(repository, times(1)).save(any(WeatherForecast.class));
    }

    @Test
    void updateForecast_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<WeatherForecast> result = weatherService.updateForecast("non-existent", validRequest);

        assertFalse(result.isPresent());
        verify(repository, never()).save(any(WeatherForecast.class));
    }

    @Test
    void deleteForecast_WhenExists_ShouldReturnTrue() {
        when(repository.existsById("test-id")).thenReturn(true);
        doNothing().when(repository).deleteById("test-id");

        boolean result = weatherService.deleteForecast("test-id");

        assertTrue(result);
        verify(repository, times(1)).deleteById("test-id");
    }

    @Test
    void deleteForecast_WhenNotExists_ShouldReturnFalse() {
        when(repository.existsById("non-existent")).thenReturn(false);

        boolean result = weatherService.deleteForecast("non-existent");

        assertFalse(result);
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void getForecastCount_ShouldReturnCount() {
        when(repository.count()).thenReturn(5L);

        long count = weatherService.getForecastCount();

        assertEquals(5L, count);
    }
}
