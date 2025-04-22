package dev.sagar.nimbus.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ApiException {
  public InvalidRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
