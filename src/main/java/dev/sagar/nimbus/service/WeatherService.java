package dev.sagar.nimbus.service;

import dev.sagar.nimbus.client.GeocodingClient;
import dev.sagar.nimbus.client.WeatherClient;
import dev.sagar.nimbus.dto.owm.GeocodingResponse;
import dev.sagar.nimbus.dto.owm.WeatherDataResponse;
import dev.sagar.nimbus.dto.response.WeatherResponse;
import dev.sagar.nimbus.exception.InvalidRequestException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
  private final GeocodingClient geocodingClient;
  private final WeatherClient weatherClient;

  public Mono<WeatherResponse> getWeatherForCity(String city) {
    if (!StringUtils.hasText(city)) {
      return Mono.error(new InvalidRequestException("City name cannot be empty"));
    }

    log.info("Processing weather request for city: {}", city);

    return geocodingClient
        .getCoordinates(city)
        .flatMap(this::getWeatherFromCoordinates)
        .map(result -> mapToWeatherResponse(result.geocoding(), result.weather()));
  }

  private Mono<GeoWeatherResult> getWeatherFromCoordinates(GeocodingResponse geocoding) {
    return weatherClient
        .getWeatherData(geocoding.getLat(), geocoding.getLon())
        .map(weather -> new GeoWeatherResult(geocoding, weather));
  }

  private WeatherResponse mapToWeatherResponse(
      GeocodingResponse geocoding, WeatherDataResponse weather) {
    String description =
        weather.getWeather() != null && !weather.getWeather().isEmpty()
            ? weather.getWeather().get(0).getDescription()
            : "No description available";

    return WeatherResponse.builder()
        .city(geocoding.getName())
        .country(geocoding.getCountry())
        .temperature(weather.getMain().getTemp())
        .feelsLike(weather.getMain().getFeelsLike())
        .humidity(weather.getMain().getHumidity())
        .windSpeed(weather.getWind().getSpeed())
        .description(description)
        .timestamp(Instant.ofEpochSecond(weather.getDt()))
        .build();
  }

  private record GeoWeatherResult(GeocodingResponse geocoding, WeatherDataResponse weather) {}
}
