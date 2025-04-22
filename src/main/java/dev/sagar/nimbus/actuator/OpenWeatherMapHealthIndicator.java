package dev.sagar.nimbus.actuator;

import dev.sagar.nimbus.config.OpenWeatherMapConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherMapHealthIndicator implements HealthIndicator {

  private final WebClient webClient;
  private final OpenWeatherMapConfig config;

  @Override
  public Health health() {
    try {
      // We'll do a simple ping to the geocoding API with a well-known city
      // just to verify connectivity
      String pingUrl =
          UriComponentsBuilder.fromUriString(config.getGeocodingUrl())
              .queryParam("q", "London")
              .queryParam("limit", 1)
              .queryParam("appid", config.getApiKey())
              .build()
              .toUriString();

      // Make a synchronous call for the health check
      String response = webClient.get().uri(pingUrl).retrieve().bodyToMono(String.class).block();

      if (response != null && !response.isEmpty()) {
        return Health.up()
            .withDetail("service", "OpenWeatherMap")
            .withDetail("status", "Available")
            .build();
      } else {
        return Health.down()
            .withDetail("service", "OpenWeatherMap")
            .withDetail("status", "Empty Response")
            .build();
      }
    } catch (Exception e) {
      log.warn("OpenWeatherMap health check failed: {}", e.getMessage());
      return Health.down()
          .withDetail("service", "OpenWeatherMap")
          .withDetail("status", "Unavailable")
          .withDetail("error", e.getMessage())
          .build();
    }
  }
}
