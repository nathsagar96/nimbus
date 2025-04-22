package dev.sagar.nimbus.controller;

import dev.sagar.nimbus.dto.response.WeatherResponse;
import dev.sagar.nimbus.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {
  private final WeatherService weatherService;

  @GetMapping
  public Mono<ResponseEntity<WeatherResponse>> getWeather(@RequestParam String city) {
    log.info("Received weather request for city: {}", city);
    return weatherService
        .getWeatherForCity(city)
        .map(ResponseEntity::ok)
        .doOnSuccess(response -> log.info("Successfully returned weather data for: {}", city));
  }
}
