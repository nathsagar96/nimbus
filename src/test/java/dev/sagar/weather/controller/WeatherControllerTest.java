package dev.sagar.weather.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.sagar.weather.dto.response.WeatherResponse;
import dev.sagar.weather.exception.CityNotFoundException;
import dev.sagar.weather.service.WeatherService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(WeatherController.class)
@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockitoBean private WeatherService weatherService;

  @Test
  void getWeather_validCity_returnsWeather() {
    // Given
    String city = "London";
    WeatherResponse expectedResponse =
        WeatherResponse.builder()
            .city("London")
            .country("GB")
            .temperature(15.5)
            .feelsLike(14.2)
            .humidity(76)
            .windSpeed(5.2)
            .description("clear sky")
            .timestamp(Instant.now())
            .build();

    when(weatherService.getWeatherForCity(anyString())).thenReturn(Mono.just(expectedResponse));

    // When & Then
    webTestClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/api/v1/weather").queryParam("city", city).build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.city")
        .isEqualTo("London")
        .jsonPath("$.country")
        .isEqualTo("GB")
        .jsonPath("$.temperature")
        .isEqualTo(15.5)
        .jsonPath("$.description")
        .isEqualTo("clear sky");
  }

  @Test
  void getWeather_cityNotFound_returns404() {
    // Given
    String nonExistentCity = "NonExistentCity";
    when(weatherService.getWeatherForCity(anyString()))
        .thenReturn(Mono.error(new CityNotFoundException(nonExistentCity)));

    // When & Then
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/api/v1/weather").queryParam("city", nonExistentCity).build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo(404)
        .jsonPath("$.message")
        .isEqualTo("City not found: " + nonExistentCity);
  }

  @Test
  void getWeather_missingCityParam_returns400() {
    // When & Then
    webTestClient
        .get()
        .uri("/api/v1/weather")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo(400)
        .jsonPath("$.message")
        .value(containsString("Required query parameter 'city' is not present."));
  }
}
