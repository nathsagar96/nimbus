package dev.sagar.weather.client;

import dev.sagar.weather.config.OpenWeatherMapConfig;
import dev.sagar.weather.dto.owm.WeatherDataResponse;
import dev.sagar.weather.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherClient {
  private final WebClient webClient;
  private final OpenWeatherMapConfig config;

  public Mono<WeatherDataResponse> getWeatherData(double lat, double lon) {
    log.info("Fetching weather data for coordinates: lat={}, lon={}", lat, lon);

    String uri =
        UriComponentsBuilder.fromUriString(config.getWeatherUrl())
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("units", config.getUnits())
            .queryParam("appid", config.getApiKey())
            .build()
            .toUriString();

    return webClient
        .get()
        .uri(uri)
        .retrieve()
        .bodyToMono(WeatherDataResponse.class)
        .doOnNext(
            response ->
                log.info(
                    "Received weather data for {}: temp={}, desc={}",
                    response.getName(),
                    response.getMain().getTemp(),
                    response.getWeather().isEmpty()
                        ? "N/A"
                        : response.getWeather().getFirst().getDescription()))
        .onErrorMap(
            e -> {
              log.error("Error fetching weather data: {}", e.getMessage());
              return new ExternalServiceException("Error from OpenWeatherMap Weather API", e);
            });
  }
}
