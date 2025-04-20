package dev.sagar.weather.client;

import dev.sagar.weather.config.OpenWeatherMapConfig;
import dev.sagar.weather.dto.owm.GeocodingResponse;
import dev.sagar.weather.exception.CityNotFoundException;
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
public class GeocodingClient {
  private final WebClient webClient;
  private final OpenWeatherMapConfig config;

  public Mono<GeocodingResponse> getCoordinates(String city) {
    log.info("Fetching coordinates for city: {}", city);

    String uri =
        UriComponentsBuilder.fromUriString(config.getGeocodingUrl())
            .queryParam("q", city)
            .queryParam("limit", 1)
            .queryParam("appid", config.getApiKey())
            .build()
            .toUriString();

    return webClient
        .get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(GeocodingResponse.class)
        .switchIfEmpty(Mono.error(new CityNotFoundException(city)))
        .next()
        .doOnNext(
            response ->
                log.info(
                    "Found coordinates for {}: lat={}, lon={}",
                    city,
                    response.getLat(),
                    response.getLon()))
        .onErrorMap(
            e -> {
              log.error("Error fetching coordinates for {}: {}", city, e.getMessage());
              if (e instanceof CityNotFoundException) {
                return e;
              }
              return new ExternalServiceException("Error from OpenWeatherMap Geocoding API", e);
            });
  }
}
