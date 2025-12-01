package com.weather.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    @Test
    void corsFilter_ShouldReturnCorsFilter() {
        CorsConfig corsConfig = new CorsConfig();
        
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        assertNotNull(corsFilter);
    }
}
