package dev.sagar.weather.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends ApiException {

  public ExternalServiceException(String message, Throwable cause) {
    super(message, cause, HttpStatus.BAD_GATEWAY);
  }
}
