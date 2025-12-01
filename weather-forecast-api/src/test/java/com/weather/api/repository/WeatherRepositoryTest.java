package com.weather.api.repository;

import com.weather.api.model.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WeatherRepositoryTest {

    private WeatherRepository repository;

    @BeforeEach
    void setUp() {
        repository = new WeatherRepository();
    }

    @Test
    void save_ShouldSaveForecastAndReturnIt() {
        WeatherForecast forecast = createForecast("New York", "USA", LocalDate.now());
        
        WeatherForecast saved = repository.save(forecast);
        
        assertNotNull(saved);
        assertEquals(forecast.getId(), saved.getId());
        assertEquals("New York", saved.getCity());
    }

    @Test
    void findById_WhenExists_ShouldReturnForecast() {
        WeatherForecast forecast = createForecast("London", "UK", LocalDate.now());
        repository.save(forecast);
        
        Optional<WeatherForecast> found = repository.findById(forecast.getId());
        
        assertTrue(found.isPresent());
        assertEquals("London", found.get().getCity());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        Optional<WeatherForecast> found = repository.findById("non-existent-id");
        
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllForecasts() {
        repository.save(createForecast("New York", "USA", LocalDate.now()));
        repository.save(createForecast("London", "UK", LocalDate.now()));
        repository.save(createForecast("Tokyo", "Japan", LocalDate.now()));
        
        List<WeatherForecast> all = repository.findAll();
        
        assertEquals(3, all.size());
    }

    @Test
    void findAll_WhenEmpty_ShouldReturnEmptyList() {
        List<WeatherForecast> all = repository.findAll();
        
        assertTrue(all.isEmpty());
    }

    @Test
    void findByCity_ShouldReturnMatchingForecasts() {
        repository.save(createForecast("New York", "USA", LocalDate.now()));
        repository.save(createForecast("New York", "USA", LocalDate.now().plusDays(1)));
        repository.save(createForecast("London", "UK", LocalDate.now()));
        
        List<WeatherForecast> found = repository.findByCity("New York");
        
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(f -> f.getCity().equals("New York")));
    }

    @Test
    void findByCity_CaseInsensitive_ShouldReturnMatchingForecasts() {
        repository.save(createForecast("New York", "USA", LocalDate.now()));
        
        List<WeatherForecast> found = repository.findByCity("new york");
        
        assertEquals(1, found.size());
    }

    @Test
    void findByCity_WhenNoMatch_ShouldReturnEmptyList() {
        repository.save(createForecast("New York", "USA", LocalDate.now()));
        
        List<WeatherForecast> found = repository.findByCity("Paris");
        
        assertTrue(found.isEmpty());
    }

    @Test
    void findByCityAndCountry_ShouldReturnMatchingForecasts() {
        repository.save(createForecast("Paris", "France", LocalDate.now()));
        repository.save(createForecast("Paris", "USA", LocalDate.now()));
        
        List<WeatherForecast> found = repository.findByCityAndCountry("Paris", "France");
        
        assertEquals(1, found.size());
        assertEquals("France", found.get(0).getCountry());
    }

    @Test
    void findByCityAndCountry_CaseInsensitive_ShouldReturnMatchingForecasts() {
        repository.save(createForecast("Paris", "France", LocalDate.now()));
        
        List<WeatherForecast> found = repository.findByCityAndCountry("paris", "france");
        
        assertEquals(1, found.size());
    }

    @Test
    void findByCityAndDate_ShouldReturnMatchingForecasts() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        repository.save(createForecast("Tokyo", "Japan", today));
        repository.save(createForecast("Tokyo", "Japan", tomorrow));
        
        List<WeatherForecast> found = repository.findByCityAndDate("Tokyo", today);
        
        assertEquals(1, found.size());
        assertEquals(today, found.get(0).getDate());
    }

    @Test
    void findByCityAndDateRange_ShouldReturnForecastsInRange() {
        LocalDate today = LocalDate.now();
        repository.save(createForecast("Sydney", "Australia", today));
        repository.save(createForecast("Sydney", "Australia", today.plusDays(1)));
        repository.save(createForecast("Sydney", "Australia", today.plusDays(2)));
        repository.save(createForecast("Sydney", "Australia", today.plusDays(5)));
        
        List<WeatherForecast> found = repository.findByCityAndDateRange("Sydney", today, today.plusDays(2));
        
        assertEquals(3, found.size());
    }

    @Test
    void findByCityAndDateRange_ShouldIncludeBoundaryDates() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        repository.save(createForecast("Berlin", "Germany", startDate));
        repository.save(createForecast("Berlin", "Germany", endDate));
        
        List<WeatherForecast> found = repository.findByCityAndDateRange("Berlin", startDate, endDate);
        
        assertEquals(2, found.size());
    }

    @Test
    void deleteById_WhenExists_ShouldRemoveForecast() {
        WeatherForecast forecast = createForecast("Madrid", "Spain", LocalDate.now());
        repository.save(forecast);
        
        repository.deleteById(forecast.getId());
        
        assertFalse(repository.findById(forecast.getId()).isPresent());
    }

    @Test
    void deleteById_WhenNotExists_ShouldNotThrowException() {
        assertDoesNotThrow(() -> repository.deleteById("non-existent-id"));
    }

    @Test
    void deleteAll_ShouldRemoveAllForecasts() {
        repository.save(createForecast("City1", "Country1", LocalDate.now()));
        repository.save(createForecast("City2", "Country2", LocalDate.now()));
        
        repository.deleteAll();
        
        assertEquals(0, repository.count());
    }

    @Test
    void existsById_WhenExists_ShouldReturnTrue() {
        WeatherForecast forecast = createForecast("Rome", "Italy", LocalDate.now());
        repository.save(forecast);
        
        assertTrue(repository.existsById(forecast.getId()));
    }

    @Test
    void existsById_WhenNotExists_ShouldReturnFalse() {
        assertFalse(repository.existsById("non-existent-id"));
    }

    @Test
    void count_ShouldReturnNumberOfForecasts() {
        repository.save(createForecast("City1", "Country1", LocalDate.now()));
        repository.save(createForecast("City2", "Country2", LocalDate.now()));
        repository.save(createForecast("City3", "Country3", LocalDate.now()));
        
        assertEquals(3, repository.count());
    }

    @Test
    void count_WhenEmpty_ShouldReturnZero() {
        assertEquals(0, repository.count());
    }

    private WeatherForecast createForecast(String city, String country, LocalDate date) {
        return new WeatherForecast(city, country, date, 20.0, "Sunny", 50, 10.0, "N", "Test description");
    }
}
