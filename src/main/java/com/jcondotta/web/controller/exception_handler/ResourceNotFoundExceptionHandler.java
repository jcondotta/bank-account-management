package com.jcondotta.web.controller.exception_handler;

import com.jcondotta.domain.exception.ResourceNotFoundException;
import com.jcondotta.web.controller.exception_handler.LocaleResolverPort;
import com.jcondotta.web.controller.exception_handler.MessageResolverPort;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.Clock;
import java.time.Instant;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundExceptionHandler.class);

    private final LocaleResolverPort localeResolverPort;
    private final MessageResolverPort messageResolverPort;
    private final Clock clock;

    public ResourceNotFoundExceptionHandler(@Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort,
                                            MessageResolverPort messageResolverPort,
                                            Clock clock) {
        this.localeResolverPort = localeResolverPort;
        this.messageResolverPort = messageResolverPort;
        this.clock = clock;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleBankAccountNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        var locale = localeResolverPort.resolveLocale();
        var message = messageResolverPort.resolveMessage(ex.getMessage(), ex.getIdentifiers(), locale);

        LOGGER.warn(message);

        var problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withTitle("Resource Not Found")
                .withDetail(message)
                .with("timestamp", Instant.now(clock))
                .build();

        return ResponseEntity
            .status(Status.NOT_FOUND.getStatusCode())
            .body(problem);
    }
}
