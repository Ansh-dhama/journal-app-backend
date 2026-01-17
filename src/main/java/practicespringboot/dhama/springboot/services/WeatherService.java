package practicespringboot.dhama.springboot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value; // Correct import
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import practicespringboot.dhama.springboot.api.response.WeatherResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {

    // Injected from application.yml
    @Value("${app.weather-api-key}")
    private String apiKey;

    @Value("${app.urls.weather-api}")
    private String weatherApiUrl; // e.g. "https://api.weatherapi.com/v1"

    // 'final' forces Lombok to create a constructor for these (Constructor Injection)
    private final RestTemplate restTemplate;
    private final RedisService redisService;

    public WeatherResponse getWeather(String city) {
        String cityKey = city.trim().toLowerCase();
        String redisKey = "weather:" + cityKey;

        // 1. Check Redis Cache
        WeatherResponse cachedResponse = redisService.get(redisKey, WeatherResponse.class);
        if (cachedResponse != null) {
            log.info("Weather cache hit for city: {}", city);
            return cachedResponse;
        }

        // 2. Build URL dynamically
        // Target: https://api.weatherapi.com/v1/current.json?key=XXX&q=CITY
        String finalUrl = String.format("%s/current.json?key=%s&q=%s", weatherApiUrl, apiKey, city);

        try {
            // 3. Call API
            WeatherResponse response = restTemplate.getForObject(finalUrl, WeatherResponse.class);

            if (response != null) {
                // 4. Save to Redis (Time To Live: 500 seconds)
                redisService.set(redisKey, response, 500L);
                log.info("Fetched weather from API for city: {}", city);
            }
            return response;

        } catch (Exception e) {
            log.error("Error fetching weather for city: {}", city, e);
            return null;
        }
    }
}