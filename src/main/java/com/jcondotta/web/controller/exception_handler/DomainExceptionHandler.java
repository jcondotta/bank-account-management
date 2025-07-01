package com.jcondotta.web.controller.exception_handler;

import com.jcondotta.domain.bankaccount.exceptions.MaxJointAccountHoldersExceededException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Clock;

@ControllerAdvice
public class DomainExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainExceptionHandler.class);

    private final LocaleResolverPort localeResolverPort;
    private final MessageResolverPort messageResolverPort;
    private final Clock clock;

    public DomainExceptionHandler(@Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort,
                                  MessageResolverPort messageResolverPort,
                                  Clock clock) {
        this.localeResolverPort = localeResolverPort;
        this.messageResolverPort = messageResolverPort;
        this.clock = clock;
    }

    @ExceptionHandler(MaxJointAccountHoldersExceededException.class)
    public ResponseEntity<String> handleDomainViolation(MaxJointAccountHoldersExceededException ex, HttpServletRequest request) {
        var locale = localeResolverPort.resolveLocale();
        var message = messageResolverPort.resolveMessage(ex.getMessage(), new Object[]{ex.getMaxAllowed()}, locale);

        LOGGER.warn(message);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(message);
    }
}
