package dev.sagar.weather.exception;

import org.springframework.http.HttpStatus;

public class CityNotFoundException extends ApiException {
  public CityNotFoundException(String city) {
    super("City not found: " + city, HttpStatus.NOT_FOUND);
  }
}
