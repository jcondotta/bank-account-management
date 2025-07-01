package com.jcondotta.web.controller.exception_handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetails(
    int status,
    String title,
    Instant timestamp,
    String path,
    Collection<FieldValidationError> errors
) {
    public static ProblemDetails validation(
        List<FieldValidationError> errors,
        String path
    ) {
        return new ProblemDetails(
            400,
            "Bad Request",
            Instant.now(),
            path,
            errors
        );
    }
}
