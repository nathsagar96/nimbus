package dev.sagar.weather.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
  private String city;
  private String country;
  private double temperature;
  private double feelsLike;
  private String description;
  private int humidity;
  private double windSpeed;
  private Instant timestamp;
}
