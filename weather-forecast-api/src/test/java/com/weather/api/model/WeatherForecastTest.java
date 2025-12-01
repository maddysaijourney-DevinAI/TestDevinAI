package com.weather.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WeatherForecastTest {

    @Test
    void constructor_Default_ShouldGenerateId() {
        WeatherForecast forecast = new WeatherForecast();
        
        assertNotNull(forecast.getId());
        assertFalse(forecast.getId().isEmpty());
    }

    @Test
    void constructor_WithParameters_ShouldSetAllFields() {
        LocalDate date = LocalDate.now();
        WeatherForecast forecast = new WeatherForecast(
                "New York", "USA", date, 20.0, "Sunny", 50, 10.0, "N", "Test description"
        );

        assertNotNull(forecast.getId());
        assertEquals("New York", forecast.getCity());
        assertEquals("USA", forecast.getCountry());
        assertEquals(date, forecast.getDate());
        assertEquals(20.0, forecast.getTemperatureCelsius());
        assertEquals("Sunny", forecast.getCondition());
        assertEquals(50, forecast.getHumidity());
        assertEquals(10.0, forecast.getWindSpeedKmh());
        assertEquals("N", forecast.getWindDirection());
        assertEquals("Test description", forecast.getDescription());
    }

    @Test
    void constructor_WithParameters_ShouldCalculateFahrenheit() {
        WeatherForecast forecast = new WeatherForecast(
                "New York", "USA", LocalDate.now(), 0.0, "Sunny", 50, 10.0, "N", "Test"
        );

        assertEquals(32.0, forecast.getTemperatureFahrenheit());
    }

    @Test
    void constructor_WithParameters_ShouldCalculateFahrenheitCorrectly() {
        WeatherForecast forecast = new WeatherForecast(
                "New York", "USA", LocalDate.now(), 100.0, "Sunny", 50, 10.0, "N", "Test"
        );

        assertEquals(212.0, forecast.getTemperatureFahrenheit());
    }

    @Test
    void constructor_WithNegativeTemperature_ShouldCalculateFahrenheitCorrectly() {
        WeatherForecast forecast = new WeatherForecast(
                "New York", "USA", LocalDate.now(), -40.0, "Sunny", 50, 10.0, "N", "Test"
        );

        assertEquals(-40.0, forecast.getTemperatureFahrenheit());
    }

    @Test
    void setId_ShouldUpdateId() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setId("custom-id");

        assertEquals("custom-id", forecast.getId());
    }

    @Test
    void setCity_ShouldUpdateCity() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setCity("London");

        assertEquals("London", forecast.getCity());
    }

    @Test
    void setCountry_ShouldUpdateCountry() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setCountry("UK");

        assertEquals("UK", forecast.getCountry());
    }

    @Test
    void setDate_ShouldUpdateDate() {
        WeatherForecast forecast = new WeatherForecast();
        LocalDate date = LocalDate.of(2025, 12, 25);
        forecast.setDate(date);

        assertEquals(date, forecast.getDate());
    }

    @Test
    void setTemperatureCelsius_ShouldUpdateBothTemperatures() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setTemperatureCelsius(25.0);

        assertEquals(25.0, forecast.getTemperatureCelsius());
        assertEquals(77.0, forecast.getTemperatureFahrenheit());
    }

    @Test
    void setTemperatureFahrenheit_ShouldUpdateFahrenheit() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setTemperatureFahrenheit(100.0);

        assertEquals(100.0, forecast.getTemperatureFahrenheit());
    }

    @Test
    void setCondition_ShouldUpdateCondition() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setCondition("Rainy");

        assertEquals("Rainy", forecast.getCondition());
    }

    @Test
    void setHumidity_ShouldUpdateHumidity() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setHumidity(75);

        assertEquals(75, forecast.getHumidity());
    }

    @Test
    void setWindSpeedKmh_ShouldUpdateWindSpeed() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setWindSpeedKmh(25.5);

        assertEquals(25.5, forecast.getWindSpeedKmh());
    }

    @Test
    void setWindDirection_ShouldUpdateWindDirection() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setWindDirection("SW");

        assertEquals("SW", forecast.getWindDirection());
    }

    @Test
    void setDescription_ShouldUpdateDescription() {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setDescription("Heavy rain expected");

        assertEquals("Heavy rain expected", forecast.getDescription());
    }

    @Test
    void temperatureConversion_ShouldRoundToTwoDecimalPlaces() {
        WeatherForecast forecast = new WeatherForecast(
                "Test", "Test", LocalDate.now(), 23.333333, "Sunny", 50, 10.0, "N", "Test"
        );

        double fahrenheit = forecast.getTemperatureFahrenheit();
        String fahrenheitStr = String.format("%.2f", fahrenheit);
        assertEquals("74.00", fahrenheitStr);
    }
}
