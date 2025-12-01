package com.weather.api.service;

import com.weather.api.model.WeatherForecast;
import com.weather.api.model.WeatherRequest;
import com.weather.api.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {
    private final WeatherRepository repository;

    public WeatherService(WeatherRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initSampleData() {
        LocalDate today = LocalDate.now();
        
        repository.save(new WeatherForecast("New York", "USA", today, 18.5, "Partly Cloudy", 65, 15.0, "NW", "Partly cloudy with mild temperatures"));
        repository.save(new WeatherForecast("New York", "USA", today.plusDays(1), 20.0, "Sunny", 55, 10.0, "W", "Clear skies expected"));
        repository.save(new WeatherForecast("New York", "USA", today.plusDays(2), 16.0, "Rainy", 80, 20.0, "NE", "Rain expected throughout the day"));
        
        repository.save(new WeatherForecast("London", "UK", today, 12.0, "Cloudy", 75, 18.0, "SW", "Overcast with occasional drizzle"));
        repository.save(new WeatherForecast("London", "UK", today.plusDays(1), 14.0, "Partly Cloudy", 70, 12.0, "W", "Clouds clearing in the afternoon"));
        repository.save(new WeatherForecast("London", "UK", today.plusDays(2), 11.0, "Rainy", 85, 25.0, "S", "Heavy rain expected"));
        
        repository.save(new WeatherForecast("Tokyo", "Japan", today, 22.0, "Sunny", 50, 8.0, "E", "Beautiful sunny day"));
        repository.save(new WeatherForecast("Tokyo", "Japan", today.plusDays(1), 24.0, "Sunny", 45, 5.0, "SE", "Hot and sunny"));
        repository.save(new WeatherForecast("Tokyo", "Japan", today.plusDays(2), 21.0, "Partly Cloudy", 60, 10.0, "N", "Some clouds moving in"));
        
        repository.save(new WeatherForecast("Sydney", "Australia", today, 28.0, "Sunny", 40, 12.0, "NE", "Hot summer day"));
        repository.save(new WeatherForecast("Sydney", "Australia", today.plusDays(1), 30.0, "Sunny", 35, 15.0, "N", "Very hot, stay hydrated"));
        repository.save(new WeatherForecast("Sydney", "Australia", today.plusDays(2), 26.0, "Thunderstorm", 70, 30.0, "W", "Afternoon thunderstorms likely"));
        
        repository.save(new WeatherForecast("Paris", "France", today, 15.0, "Cloudy", 68, 14.0, "W", "Mild with cloud cover"));
        repository.save(new WeatherForecast("Paris", "France", today.plusDays(1), 17.0, "Partly Cloudy", 60, 10.0, "SW", "Pleasant day expected"));
        repository.save(new WeatherForecast("Paris", "France", today.plusDays(2), 14.0, "Rainy", 78, 18.0, "NW", "Rain moving in from the west"));
    }

    public WeatherForecast createForecast(WeatherRequest request) {
        WeatherForecast forecast = new WeatherForecast(
                request.getCity(),
                request.getCountry(),
                request.getDate(),
                request.getTemperatureCelsius(),
                request.getCondition(),
                request.getHumidity(),
                request.getWindSpeedKmh(),
                request.getWindDirection(),
                request.getDescription()
        );
        return repository.save(forecast);
    }

    public Optional<WeatherForecast> getForecastById(String id) {
        return repository.findById(id);
    }

    public List<WeatherForecast> getAllForecasts() {
        return repository.findAll();
    }

    public List<WeatherForecast> getForecastsByCity(String city) {
        return repository.findByCity(city);
    }

    public List<WeatherForecast> getForecastsByCityAndCountry(String city, String country) {
        return repository.findByCityAndCountry(city, country);
    }

    public List<WeatherForecast> getForecastsByCityAndDate(String city, LocalDate date) {
        return repository.findByCityAndDate(city, date);
    }

    public List<WeatherForecast> getForecastsByCityAndDateRange(String city, LocalDate startDate, LocalDate endDate) {
        return repository.findByCityAndDateRange(city, startDate, endDate);
    }

    public Optional<WeatherForecast> updateForecast(String id, WeatherRequest request) {
        return repository.findById(id).map(existing -> {
            existing.setCity(request.getCity());
            existing.setCountry(request.getCountry());
            existing.setDate(request.getDate());
            existing.setTemperatureCelsius(request.getTemperatureCelsius());
            existing.setCondition(request.getCondition());
            existing.setHumidity(request.getHumidity());
            existing.setWindSpeedKmh(request.getWindSpeedKmh());
            existing.setWindDirection(request.getWindDirection());
            existing.setDescription(request.getDescription());
            return repository.save(existing);
        });
    }

    public boolean deleteForecast(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getForecastCount() {
        return repository.count();
    }
}
