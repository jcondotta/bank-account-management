package com.jcondotta.web.controller.exception_handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Produces
@Singleton
@Requires(classes = { Exception.class, ExceptionHandler.class })
public class RestExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);
    private final ErrorResponseProcessor<?> errorResponseProcessor;

    @Inject
    public RestExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor) {
        this.errorResponseProcessor = errorResponseProcessor;
    }

    @Override
    @Status(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        LOGGER.error("Unexpected error occurred: {}", exception.getMessage(), exception);

        List<String> errorMessages = List.of("An unexpected error occurred. Please try again later.");

        var responseBody = Map.of(
                "_embedded", Map.of("errors", errorMessages.stream()
                        .map(msg -> Map.of("message", msg))
                        .toList()),
                "message", "Internal Server Error"
        );

        return errorResponseProcessor.processResponse(
                ErrorContext.builder(request)
                        .cause(exception)
                        .errorMessages(errorMessages)
                        .build(),
                HttpResponse.serverError().body(responseBody)
        );
    }
}
