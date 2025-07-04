package com.jcondotta.interfaces.rest.exception_handler;

import com.jcondotta.domain.shared.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Clock;

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
    public ResponseEntity<ProblemDetails> handleBankAccountNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        var locale = localeResolverPort.resolveLocale();
        var message = messageResolverPort.resolveMessage(ex.getMessage(), ex.getIdentifiers(), locale);

        LOGGER.warn(message);

//        var problem = new ProblemDetails(
//                HttpStatus.NOT_FOUND.value(),
//                HttpStatus.NOT_FOUND.getReasonPhrase(),
//                Instant.now(clock),
//                request.getRequestURI(),
//                null
//        );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND.value())
            .body(null);
    }
}
