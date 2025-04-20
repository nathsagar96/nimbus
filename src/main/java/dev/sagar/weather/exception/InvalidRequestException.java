package dev.sagar.weather.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ApiException {
  public InvalidRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
