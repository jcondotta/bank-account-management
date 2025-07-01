package com.jcondotta.interfaces.rest;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public interface ValidatableRequest {

    Logger LOGGER = LoggerFactory.getLogger(ValidatableRequest.class);

    default void validateWith(Validator validator) {
        LOGGER.trace("Validating: {} properties", this.getClass().getSimpleName());

        var constraintViolations = validator.validate(this);

        if (!constraintViolations.isEmpty()) {
            String validationMessages = constraintViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            LOGGER.warn("Validation failed for {}: [{}]", this.getClass().getSimpleName(), validationMessages);
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}