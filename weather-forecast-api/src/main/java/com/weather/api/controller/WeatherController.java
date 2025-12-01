package com.weather.api.controller;

import com.weather.api.model.WeatherForecast;
import com.weather.api.model.WeatherRequest;
import com.weather.api.service.WeatherService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Weather Forecast API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDate.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WeatherForecast>> getAllForecasts() {
        return ResponseEntity.ok(weatherService.getAllForecasts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherForecast> getForecastById(@PathVariable String id) {
        return weatherService.getForecastById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<WeatherForecast>> getForecastsByCity(@PathVariable String city) {
        List<WeatherForecast> forecasts = weatherService.getForecastsByCity(city);
        if (forecasts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/city/{city}/country/{country}")
    public ResponseEntity<List<WeatherForecast>> getForecastsByCityAndCountry(
            @PathVariable String city,
            @PathVariable String country) {
        List<WeatherForecast> forecasts = weatherService.getForecastsByCityAndCountry(city, country);
        if (forecasts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/city/{city}/date/{date}")
    public ResponseEntity<List<WeatherForecast>> getForecastsByCityAndDate(
            @PathVariable String city,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<WeatherForecast> forecasts = weatherService.getForecastsByCityAndDate(city, date);
        if (forecasts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/city/{city}/range")
    public ResponseEntity<List<WeatherForecast>> getForecastsByCityAndDateRange(
            @PathVariable String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        List<WeatherForecast> forecasts = weatherService.getForecastsByCityAndDateRange(city, startDate, endDate);
        if (forecasts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecasts);
    }

    @PostMapping
    public ResponseEntity<WeatherForecast> createForecast(@Valid @RequestBody WeatherRequest request) {
        WeatherForecast created = weatherService.createForecast(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherForecast> updateForecast(
            @PathVariable String id,
            @Valid @RequestBody WeatherRequest request) {
        return weatherService.updateForecast(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForecast(@PathVariable String id) {
        if (weatherService.deleteForecast(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalForecasts", weatherService.getForecastCount());
        stats.put("timestamp", LocalDate.now().toString());
        return ResponseEntity.ok(stats);
    }
}
