package com.weather.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weather.api.model.WeatherForecast;
import com.weather.api.model.WeatherRequest;
import com.weather.api.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private ObjectMapper objectMapper;
    private WeatherForecast sampleForecast;
    private WeatherRequest validRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleForecast = new WeatherForecast("New York", "USA", LocalDate.now(), 20.0, "Sunny", 50, 10.0, "N", "Test description");

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
    }

    @Test
    void healthCheck_ShouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/weather/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("Weather Forecast API")))
                .andExpect(jsonPath("$.version", is("1.0.0")));
    }

    @Test
    void getAllForecasts_ShouldReturnAllForecasts() throws Exception {
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(weatherService.getAllForecasts()).thenReturn(forecasts);

        mockMvc.perform(get("/api/weather"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].city", is("New York")));
    }

    @Test
    void getForecastById_WhenExists_ShouldReturnForecast() throws Exception {
        when(weatherService.getForecastById("test-id")).thenReturn(Optional.of(sampleForecast));

        mockMvc.perform(get("/api/weather/test-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("New York")))
                .andExpect(jsonPath("$.country", is("USA")));
    }

    @Test
    void getForecastById_WhenNotExists_ShouldReturn404() throws Exception {
        when(weatherService.getForecastById("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/weather/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getForecastsByCity_WhenExists_ShouldReturnForecasts() throws Exception {
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(weatherService.getForecastsByCity("New York")).thenReturn(forecasts);

        mockMvc.perform(get("/api/weather/city/New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].city", is("New York")));
    }

    @Test
    void getForecastsByCity_WhenNotExists_ShouldReturn404() throws Exception {
        when(weatherService.getForecastsByCity("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/weather/city/Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getForecastsByCityAndCountry_WhenExists_ShouldReturnForecasts() throws Exception {
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(weatherService.getForecastsByCityAndCountry("New York", "USA")).thenReturn(forecasts);

        mockMvc.perform(get("/api/weather/city/New York/country/USA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].country", is("USA")));
    }

    @Test
    void getForecastsByCityAndCountry_WhenNotExists_ShouldReturn404() throws Exception {
        when(weatherService.getForecastsByCityAndCountry("Unknown", "Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/weather/city/Unknown/country/Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getForecastsByCityAndDate_WhenExists_ShouldReturnForecasts() throws Exception {
        LocalDate today = LocalDate.now();
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(weatherService.getForecastsByCityAndDate("New York", today)).thenReturn(forecasts);

        mockMvc.perform(get("/api/weather/city/New York/date/" + today))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getForecastsByCityAndDate_WhenNotExists_ShouldReturn404() throws Exception {
        LocalDate today = LocalDate.now();
        when(weatherService.getForecastsByCityAndDate("Unknown", today)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/weather/city/Unknown/date/" + today))
                .andExpect(status().isNotFound());
    }

    @Test
    void getForecastsByCityAndDateRange_WhenExists_ShouldReturnForecasts() throws Exception {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        List<WeatherForecast> forecasts = Arrays.asList(sampleForecast);
        when(weatherService.getForecastsByCityAndDateRange("New York", startDate, endDate)).thenReturn(forecasts);

        mockMvc.perform(get("/api/weather/city/New York/range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getForecastsByCityAndDateRange_WhenNotExists_ShouldReturn404() throws Exception {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        when(weatherService.getForecastsByCityAndDateRange("Unknown", startDate, endDate)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/weather/city/Unknown/range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getForecastsByCityAndDateRange_WhenStartDateAfterEndDate_ShouldReturn400() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = LocalDate.now();

        mockMvc.perform(get("/api/weather/city/New York/range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithValidRequest_ShouldReturn201() throws Exception {
        when(weatherService.createForecast(any(WeatherRequest.class))).thenReturn(sampleForecast);

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city", is("New York")));
    }

    @Test
    void createForecast_WithMissingCity_ShouldReturn400() throws Exception {
        validRequest.setCity("");

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithMissingCountry_ShouldReturn400() throws Exception {
        validRequest.setCountry("");

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithMissingCondition_ShouldReturn400() throws Exception {
        validRequest.setCondition("");

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithInvalidHumidity_ShouldReturn400() throws Exception {
        validRequest.setHumidity(150);

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithNegativeHumidity_ShouldReturn400() throws Exception {
        validRequest.setHumidity(-10);

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithInvalidTemperature_ShouldReturn400() throws Exception {
        validRequest.setTemperatureCelsius(100.0);

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createForecast_WithNegativeWindSpeed_ShouldReturn400() throws Exception {
        validRequest.setWindSpeedKmh(-5.0);

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateForecast_WhenExists_ShouldReturnUpdatedForecast() throws Exception {
        when(weatherService.updateForecast(eq("test-id"), any(WeatherRequest.class))).thenReturn(Optional.of(sampleForecast));

        mockMvc.perform(put("/api/weather/test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("New York")));
    }

    @Test
    void updateForecast_WhenNotExists_ShouldReturn404() throws Exception {
        when(weatherService.updateForecast(eq("non-existent"), any(WeatherRequest.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/weather/non-existent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteForecast_WhenExists_ShouldReturn204() throws Exception {
        when(weatherService.deleteForecast("test-id")).thenReturn(true);

        mockMvc.perform(delete("/api/weather/test-id"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteForecast_WhenNotExists_ShouldReturn404() throws Exception {
        when(weatherService.deleteForecast("non-existent")).thenReturn(false);

        mockMvc.perform(delete("/api/weather/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStats_ShouldReturnStatistics() throws Exception {
        when(weatherService.getForecastCount()).thenReturn(10L);

        mockMvc.perform(get("/api/weather/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalForecasts", is(10)));
    }
}
