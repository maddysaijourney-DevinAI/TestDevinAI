package com.weather.api.model;

import java.time.LocalDate;
import java.util.UUID;

public class WeatherForecast {
    private String id;
    private String city;
    private String country;
    private LocalDate date;
    private double temperatureCelsius;
    private double temperatureFahrenheit;
    private String condition;
    private int humidity;
    private double windSpeedKmh;
    private String windDirection;
    private String description;

    public WeatherForecast() {
        this.id = UUID.randomUUID().toString();
    }

    public WeatherForecast(String city, String country, LocalDate date, double temperatureCelsius,
                           String condition, int humidity, double windSpeedKmh, String windDirection, String description) {
        this.id = UUID.randomUUID().toString();
        this.city = city;
        this.country = country;
        this.date = date;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = celsiusToFahrenheit(temperatureCelsius);
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeedKmh = windSpeedKmh;
        this.windDirection = windDirection;
        this.description = description;
    }

    private double celsiusToFahrenheit(double celsius) {
        return Math.round((celsius * 9.0 / 5.0 + 32) * 100.0) / 100.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = celsiusToFahrenheit(temperatureCelsius);
    }

    public double getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public void setTemperatureFahrenheit(double temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public void setWindSpeedKmh(double windSpeedKmh) {
        this.windSpeedKmh = windSpeedKmh;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
