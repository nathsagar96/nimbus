package dev.sagar.nimbus.service;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import dev.sagar.nimbus.client.GeocodingClient;
import dev.sagar.nimbus.client.WeatherClient;
import dev.sagar.nimbus.dto.owm.GeocodingResponse;
import dev.sagar.nimbus.dto.owm.WeatherDataResponse;
import dev.sagar.nimbus.exception.CityNotFoundException;
import dev.sagar.nimbus.exception.InvalidRequestException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

  @Mock private GeocodingClient geocodingClient;

  @Mock private WeatherClient weatherClient;

  @InjectMocks private WeatherService weatherService;

  private GeocodingResponse geocodingResponse;
  private WeatherDataResponse weatherDataResponse;

  @BeforeEach
  void setUp() {
    // Setup geocoding response
    geocodingResponse = new GeocodingResponse();
    geocodingResponse.setName("London");
    geocodingResponse.setCountry("GB");
    geocodingResponse.setLat(51.5074);
    geocodingResponse.setLon(-0.1278);

    // Setup weather data response
    weatherDataResponse = new WeatherDataResponse();

    WeatherDataResponse.Main main = new WeatherDataResponse.Main();
    main.setTemp(15.5);
    main.setFeelsLike(14.2);
    main.setHumidity(76);
    weatherDataResponse.setMain(main);

    WeatherDataResponse.Wind wind = new WeatherDataResponse.Wind();
    wind.setSpeed(5.2);
    weatherDataResponse.setWind(wind);

    WeatherDataResponse.Weather weather = new WeatherDataResponse.Weather();
    weather.setDescription("clear sky");
    weatherDataResponse.setWeather(List.of(weather));

    weatherDataResponse.setDt(System.currentTimeMillis() / 1000);
  }

  @Test
  void getWeatherForCity_validCity_returnsWeatherData() {
    // Given
    String city = "London";
    when(geocodingClient.getCoordinates(eq(city))).thenReturn(Mono.just(geocodingResponse));
    when(weatherClient.getWeatherData(anyDouble(), anyDouble()))
        .thenReturn(Mono.just(weatherDataResponse));

    // When & Then
    StepVerifier.create(weatherService.getWeatherForCity(city))
        .expectNextMatches(
            response ->
                response.getCity().equals("London")
                    && response.getCountry().equals("GB")
                    && response.getTemperature() == 15.5
                    && response.getFeelsLike() == 14.2
                    && response.getHumidity() == 76
                    && response.getWindSpeed() == 5.2
                    && response.getDescription().equals("clear sky"))
        .verifyComplete();
  }

  @Test
  void getWeatherForCity_emptyCity_throwsInvalidRequestException() {
    // When & Then
    StepVerifier.create(weatherService.getWeatherForCity(""))
        .expectError(InvalidRequestException.class)
        .verify();
  }

  @Test
  void getWeatherForCity_cityNotFound_throwsCityNotFoundException() {
    // Given
    String city = "NonExistentCity";
    when(geocodingClient.getCoordinates(eq(city)))
        .thenReturn(Mono.error(new CityNotFoundException(city)));

    // When & Then
    StepVerifier.create(weatherService.getWeatherForCity(city))
        .expectError(CityNotFoundException.class)
        .verify();
  }
}
