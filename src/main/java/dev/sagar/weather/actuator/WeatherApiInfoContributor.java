package dev.sagar.weather.actuator;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class WeatherApiInfoContributor implements InfoContributor {

  @Override
  public void contribute(Info.Builder builder) {
    Map<String, Object> apiDetails = new HashMap<>();
    apiDetails.put("name", "OpenWeatherMap");
    apiDetails.put("description", "Provider of weather data");
    apiDetails.put(
        "endpoints",
        Map.of(
            "geocoding", "Used for converting city names to coordinates",
            "weather", "Used for retrieving current weather data"));

    builder.withDetail("external-apis", apiDetails);
  }
}
