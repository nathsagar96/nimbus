package dev.sagar.weather.exception;

import dev.sagar.weather.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleApiException(
      ApiException ex, ServerWebExchange exchange) {
    log.error("API Exception: {}", ex.getMessage());
    return buildErrorResponse(
        ex.getMessage(), ex.getStatus().value(), exchange.getRequest().getPath().value());
  }

  @ExceptionHandler(MissingRequestValueException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleMissingRequestValueException(
      MissingRequestValueException ex, ServerWebExchange exchange) {
    log.error("Missing request value: {}", ex.getMessage());
    return buildErrorResponse(
        ex.getReason(), 400, exchange.getRequest().getPath().value());
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleWebExchangeBindException(
      WebExchangeBindException ex, ServerWebExchange exchange) {
    log.error("Validation error: {}", ex.getMessage());
    String errorMessage =
        ex.getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.joining(", "));
    return buildErrorResponse(errorMessage, 400, exchange.getRequest().getPath().value());
  }

  @ExceptionHandler(WebClientResponseException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleWebClientResponseException(
      WebClientResponseException ex, ServerWebExchange exchange) {
    log.error("External API error: {} {}", ex.getStatusCode(), ex.getMessage());
    String message = "Error from external API: " + ex.getStatusCode() + " " + ex.getStatusText();
    return buildErrorResponse(message, 502, exchange.getRequest().getPath().value());
  }

  @ExceptionHandler(WebClientException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleWebClientException(
      WebClientException ex, ServerWebExchange exchange) {
    log.error("WebClient error: {}", ex.getMessage());
    return buildErrorResponse(
        "Error communicating with external API", 503, exchange.getRequest().getPath().value());
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
      Exception ex, ServerWebExchange exchange) {
    log.error("Unhandled exception", ex);
    return buildErrorResponse(
        "Internal server error", 500, exchange.getRequest().getPath().value());
  }

  private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(
      String message, int status, String path) {
    ErrorResponse error =
        ErrorResponse.builder()
            .status(status)
            .message(message)
            .path(path)
            .timestamp(Instant.now())
            .build();
    return Mono.just(ResponseEntity.status(status).body(error));
  }
}
