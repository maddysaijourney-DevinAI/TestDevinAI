# Weather Forecast API

A RESTful Weather Forecast API built with Java 11 and Spring Boot 2.7. This API provides endpoints to manage and retrieve weather forecast data for various cities around the world.

## Table of Contents

1. [Overview](#overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [API Endpoints](#api-endpoints)
5. [Data Models](#data-models)
6. [Getting Started](#getting-started)
7. [Configuration](#configuration)
8. [Testing](#testing)
9. [Deployment](#deployment)

## Overview

The Weather Forecast API is a proof-of-concept application that demonstrates a RESTful service for managing weather forecast data. It uses an in-memory data store, which means data will be reset when the application restarts. The API comes pre-loaded with sample weather data for major cities including New York, London, Tokyo, Sydney, and Paris.

### Key Features

- Full CRUD operations for weather forecasts
- Query forecasts by city, country, date, or date range
- Automatic temperature conversion between Celsius and Fahrenheit
- CORS enabled for cross-origin requests
- Input validation on all request payloads
- Health check and statistics endpoints

## Technology Stack

- **Language**: Java 11
- **Framework**: Spring Boot 2.7.18
- **Build Tool**: Maven 3.6+
- **Dependencies**:
  - Spring Web (REST API)
  - Spring Validation (Input validation)
  - Jackson (JSON serialization)

## Project Structure

```
weather-forecast-api/
├── pom.xml                                    # Maven configuration
├── Dockerfile                                 # Docker configuration
├── README.md                                  # This documentation
├── src/
│   └── main/
│       ├── java/
│       │   └── com/weather/api/
│       │       ├── WeatherForecastApplication.java    # Main application class
│       │       ├── config/
│       │       │   └── CorsConfig.java                # CORS configuration
│       │       ├── controller/
│       │       │   └── WeatherController.java         # REST controller
│       │       ├── model/
│       │       │   ├── WeatherForecast.java           # Forecast entity
│       │       │   └── WeatherRequest.java            # Request DTO
│       │       ├── repository/
│       │       │   └── WeatherRepository.java         # In-memory repository
│       │       └── service/
│       │           └── WeatherService.java            # Business logic
│       └── resources/
│           └── application.properties                 # Application config
└── target/
    └── weather-forecast-api-1.0.0.jar                # Compiled JAR
```

## API Endpoints

### Health Check

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weather/health` | Check API health status |

**Response Example:**
```json
{
  "status": "UP",
  "service": "Weather Forecast API",
  "version": "1.0.0",
  "timestamp": "2025-12-01"
}
```

### Weather Forecasts

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weather` | Get all weather forecasts |
| GET | `/api/weather/{id}` | Get forecast by ID |
| GET | `/api/weather/city/{city}` | Get forecasts by city name |
| GET | `/api/weather/city/{city}/country/{country}` | Get forecasts by city and country |
| GET | `/api/weather/city/{city}/date/{date}` | Get forecasts by city and specific date |
| GET | `/api/weather/city/{city}/range?startDate={start}&endDate={end}` | Get forecasts by city and date range |
| POST | `/api/weather` | Create a new forecast |
| PUT | `/api/weather/{id}` | Update an existing forecast |
| DELETE | `/api/weather/{id}` | Delete a forecast |

### Statistics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weather/stats` | Get forecast statistics |

## Data Models

### WeatherForecast

The main entity representing a weather forecast:

| Field | Type | Description |
|-------|------|-------------|
| id | String | Unique identifier (UUID) |
| city | String | City name |
| country | String | Country name |
| date | LocalDate | Forecast date (YYYY-MM-DD) |
| temperatureCelsius | double | Temperature in Celsius |
| temperatureFahrenheit | double | Temperature in Fahrenheit (auto-calculated) |
| condition | String | Weather condition (e.g., Sunny, Rainy, Cloudy) |
| humidity | int | Humidity percentage (0-100) |
| windSpeedKmh | double | Wind speed in km/h |
| windDirection | String | Wind direction (e.g., N, NE, E, SE, S, SW, W, NW) |
| description | String | Detailed weather description |

### WeatherRequest

Request payload for creating/updating forecasts:

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| city | String | Yes | Not blank |
| country | String | Yes | Not blank |
| date | LocalDate | Yes | Not null |
| temperatureCelsius | double | Yes | Between -100 and 60 |
| condition | String | Yes | Not blank |
| humidity | int | Yes | Between 0 and 100 |
| windSpeedKmh | double | Yes | Minimum 0 |
| windDirection | String | No | - |
| description | String | No | - |

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Building the Application

```bash
# Clone or navigate to the project directory
cd weather-forecast-api

# Build the project
mvn clean package

# The JAR file will be created at target/weather-forecast-api-1.0.0.jar
```

### Running the Application

```bash
# Run using Maven
mvn spring-boot:run

# Or run the JAR directly
java -jar target/weather-forecast-api-1.0.0.jar
```

The application will start on port 8080 by default.

### Quick Test

```bash
# Check health
curl http://localhost:8080/api/weather/health

# Get all forecasts
curl http://localhost:8080/api/weather

# Get forecasts for a specific city
curl http://localhost:8080/api/weather/city/London

# Create a new forecast
curl -X POST http://localhost:8080/api/weather \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Berlin",
    "country": "Germany",
    "date": "2025-12-01",
    "temperatureCelsius": 5.0,
    "condition": "Snowy",
    "humidity": 80,
    "windSpeedKmh": 20.0,
    "windDirection": "N",
    "description": "Light snow expected"
  }'
```

## Configuration

### Application Properties

The application can be configured via `src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| server.port | 8080 | Server port |
| spring.application.name | weather-forecast-api | Application name |
| spring.jackson.serialization.write-dates-as-timestamps | false | Date format as ISO string |

### Environment Variables

You can override properties using environment variables:

```bash
# Change the server port
export SERVER_PORT=9090
java -jar target/weather-forecast-api-1.0.0.jar
```

## Testing

### Using cURL

```bash
# Health check
curl -s http://localhost:8080/api/weather/health | jq

# Get all forecasts
curl -s http://localhost:8080/api/weather | jq

# Get forecast by ID
curl -s http://localhost:8080/api/weather/{forecast-id} | jq

# Get forecasts by city
curl -s http://localhost:8080/api/weather/city/Tokyo | jq

# Get forecasts by city and country
curl -s http://localhost:8080/api/weather/city/London/country/UK | jq

# Get forecasts by date range
curl -s "http://localhost:8080/api/weather/city/Paris/range?startDate=2025-12-01&endDate=2025-12-03" | jq

# Create forecast
curl -s -X POST http://localhost:8080/api/weather \
  -H "Content-Type: application/json" \
  -d '{"city":"Miami","country":"USA","date":"2025-12-01","temperatureCelsius":28.0,"condition":"Sunny","humidity":70,"windSpeedKmh":15.0,"windDirection":"SE","description":"Warm and sunny"}' | jq

# Update forecast
curl -s -X PUT http://localhost:8080/api/weather/{forecast-id} \
  -H "Content-Type: application/json" \
  -d '{"city":"Miami","country":"USA","date":"2025-12-01","temperatureCelsius":30.0,"condition":"Hot","humidity":75,"windSpeedKmh":10.0,"windDirection":"S","description":"Very hot day"}' | jq

# Delete forecast
curl -s -X DELETE http://localhost:8080/api/weather/{forecast-id}

# Get statistics
curl -s http://localhost:8080/api/weather/stats | jq
```

## Deployment

### Using Docker

Build and run the Docker container:

```bash
# Build the JAR first
mvn clean package -DskipTests

# Build Docker image
docker build -t weather-forecast-api .

# Run container
docker run -p 8080:8080 weather-forecast-api
```

### Docker Compose (Optional)

Create a `docker-compose.yml`:

```yaml
version: '3.8'
services:
  weather-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - JAVA_OPTS=-Xmx512m
```

Run with:
```bash
docker-compose up -d
```

### Production Considerations

1. **Data Persistence**: This proof-of-concept uses in-memory storage. For production, integrate with a database (PostgreSQL, MySQL, etc.)

2. **Security**: Add authentication/authorization (Spring Security, OAuth2, JWT)

3. **Monitoring**: Add actuator endpoints for health monitoring

4. **Logging**: Configure proper logging levels and log aggregation

5. **Rate Limiting**: Implement rate limiting for public APIs

## Sample Data

The API comes pre-loaded with weather forecasts for the following cities:

- New York, USA (3 days)
- London, UK (3 days)
- Tokyo, Japan (3 days)
- Sydney, Australia (3 days)
- Paris, France (3 days)

## License

This project is provided as-is for demonstration purposes.

## Support

For questions or issues, please contact the development team.
