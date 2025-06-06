package com.jcondotta.web.controller.exception_handler;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.Clock;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodArgumentNotValidExceptionHandler.class);

    private final ValidationErrorConverterPort<FieldError, FieldValidationError> validationErrorMapper;
    private final Clock clock;

    public MethodArgumentNotValidExceptionHandler(ValidationErrorConverterPort<FieldError, FieldValidationError> validationErrorMapper, Clock clock) {
        this.validationErrorMapper = validationErrorMapper;
        this.clock = clock;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var errors = validationErrorMapper.apply(ex.getBindingResult().getFieldErrors());

        LOGGER.warn("Validation error for request: {}, Errors: [{}]",
                request.getRequestURI(),
                errors.stream()
                        .map(error -> String.format("[field=%s, errorMessages=%s]", error.field(), error.messages()))
                        .collect(Collectors.joining(", "))
        );

        var problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(Status.BAD_REQUEST.getReasonPhrase())
                .with("timestamp", Instant.now(clock))
                .with("path", request.getRequestURI())
                .with("errors", errors)
                .build();

        return ResponseEntity.badRequest().body(problem);
    }
}