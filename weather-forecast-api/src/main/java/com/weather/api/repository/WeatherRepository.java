package com.weather.api.repository;

import com.weather.api.model.WeatherForecast;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class WeatherRepository {
    private final Map<String, WeatherForecast> forecasts = new ConcurrentHashMap<>();

    public WeatherForecast save(WeatherForecast forecast) {
        forecasts.put(forecast.getId(), forecast);
        return forecast;
    }

    public Optional<WeatherForecast> findById(String id) {
        return Optional.ofNullable(forecasts.get(id));
    }

    public List<WeatherForecast> findAll() {
        return new ArrayList<>(forecasts.values());
    }

    public List<WeatherForecast> findByCity(String city) {
        return forecasts.values().stream()
                .filter(f -> f.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    public List<WeatherForecast> findByCityAndCountry(String city, String country) {
        return forecasts.values().stream()
                .filter(f -> f.getCity().equalsIgnoreCase(city) && f.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    public List<WeatherForecast> findByCityAndDate(String city, LocalDate date) {
        return forecasts.values().stream()
                .filter(f -> f.getCity().equalsIgnoreCase(city) && f.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<WeatherForecast> findByCityAndDateRange(String city, LocalDate startDate, LocalDate endDate) {
        return forecasts.values().stream()
                .filter(f -> f.getCity().equalsIgnoreCase(city) &&
                        !f.getDate().isBefore(startDate) &&
                        !f.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        forecasts.remove(id);
    }

    public void deleteAll() {
        forecasts.clear();
    }

    public boolean existsById(String id) {
        return forecasts.containsKey(id);
    }

    public long count() {
        return forecasts.size();
    }
}
