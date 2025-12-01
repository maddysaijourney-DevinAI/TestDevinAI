package com.weather.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WeatherRequestTest {

    @Test
    void setCity_ShouldUpdateCity() {
        WeatherRequest request = new WeatherRequest();
        request.setCity("Tokyo");

        assertEquals("Tokyo", request.getCity());
    }

    @Test
    void getCity_ShouldReturnCity() {
        WeatherRequest request = new WeatherRequest();
        request.setCity("Paris");

        assertEquals("Paris", request.getCity());
    }

    @Test
    void setCountry_ShouldUpdateCountry() {
        WeatherRequest request = new WeatherRequest();
        request.setCountry("Japan");

        assertEquals("Japan", request.getCountry());
    }

    @Test
    void getCountry_ShouldReturnCountry() {
        WeatherRequest request = new WeatherRequest();
        request.setCountry("France");

        assertEquals("France", request.getCountry());
    }

    @Test
    void setDate_ShouldUpdateDate() {
        WeatherRequest request = new WeatherRequest();
        LocalDate date = LocalDate.of(2025, 6, 15);
        request.setDate(date);

        assertEquals(date, request.getDate());
    }

    @Test
    void getDate_ShouldReturnDate() {
        WeatherRequest request = new WeatherRequest();
        LocalDate date = LocalDate.now();
        request.setDate(date);

        assertEquals(date, request.getDate());
    }

    @Test
    void setTemperatureCelsius_ShouldUpdateTemperature() {
        WeatherRequest request = new WeatherRequest();
        request.setTemperatureCelsius(30.5);

        assertEquals(30.5, request.getTemperatureCelsius());
    }

    @Test
    void getTemperatureCelsius_ShouldReturnTemperature() {
        WeatherRequest request = new WeatherRequest();
        request.setTemperatureCelsius(-10.0);

        assertEquals(-10.0, request.getTemperatureCelsius());
    }

    @Test
    void setCondition_ShouldUpdateCondition() {
        WeatherRequest request = new WeatherRequest();
        request.setCondition("Thunderstorm");

        assertEquals("Thunderstorm", request.getCondition());
    }

    @Test
    void getCondition_ShouldReturnCondition() {
        WeatherRequest request = new WeatherRequest();
        request.setCondition("Snowy");

        assertEquals("Snowy", request.getCondition());
    }

    @Test
    void setHumidity_ShouldUpdateHumidity() {
        WeatherRequest request = new WeatherRequest();
        request.setHumidity(85);

        assertEquals(85, request.getHumidity());
    }

    @Test
    void getHumidity_ShouldReturnHumidity() {
        WeatherRequest request = new WeatherRequest();
        request.setHumidity(0);

        assertEquals(0, request.getHumidity());
    }

    @Test
    void setWindSpeedKmh_ShouldUpdateWindSpeed() {
        WeatherRequest request = new WeatherRequest();
        request.setWindSpeedKmh(45.0);

        assertEquals(45.0, request.getWindSpeedKmh());
    }

    @Test
    void getWindSpeedKmh_ShouldReturnWindSpeed() {
        WeatherRequest request = new WeatherRequest();
        request.setWindSpeedKmh(0.0);

        assertEquals(0.0, request.getWindSpeedKmh());
    }

    @Test
    void setWindDirection_ShouldUpdateWindDirection() {
        WeatherRequest request = new WeatherRequest();
        request.setWindDirection("NE");

        assertEquals("NE", request.getWindDirection());
    }

    @Test
    void getWindDirection_ShouldReturnWindDirection() {
        WeatherRequest request = new WeatherRequest();
        request.setWindDirection("SE");

        assertEquals("SE", request.getWindDirection());
    }

    @Test
    void setDescription_ShouldUpdateDescription() {
        WeatherRequest request = new WeatherRequest();
        request.setDescription("Clear skies with light breeze");

        assertEquals("Clear skies with light breeze", request.getDescription());
    }

    @Test
    void getDescription_ShouldReturnDescription() {
        WeatherRequest request = new WeatherRequest();
        request.setDescription("Foggy morning");

        assertEquals("Foggy morning", request.getDescription());
    }

    @Test
    void allFieldsCanBeSetAndRetrieved() {
        WeatherRequest request = new WeatherRequest();
        LocalDate date = LocalDate.now();

        request.setCity("Sydney");
        request.setCountry("Australia");
        request.setDate(date);
        request.setTemperatureCelsius(28.0);
        request.setCondition("Sunny");
        request.setHumidity(40);
        request.setWindSpeedKmh(15.0);
        request.setWindDirection("N");
        request.setDescription("Beautiful sunny day");

        assertEquals("Sydney", request.getCity());
        assertEquals("Australia", request.getCountry());
        assertEquals(date, request.getDate());
        assertEquals(28.0, request.getTemperatureCelsius());
        assertEquals("Sunny", request.getCondition());
        assertEquals(40, request.getHumidity());
        assertEquals(15.0, request.getWindSpeedKmh());
        assertEquals("N", request.getWindDirection());
        assertEquals("Beautiful sunny day", request.getDescription());
    }

    @Test
    void defaultValues_ShouldBeNullOrZero() {
        WeatherRequest request = new WeatherRequest();

        assertNull(request.getCity());
        assertNull(request.getCountry());
        assertNull(request.getDate());
        assertEquals(0.0, request.getTemperatureCelsius());
        assertNull(request.getCondition());
        assertEquals(0, request.getHumidity());
        assertEquals(0.0, request.getWindSpeedKmh());
        assertNull(request.getWindDirection());
        assertNull(request.getDescription());
    }
}
