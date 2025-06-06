package com.jcondotta.adapter.in.web.exception_handler;

import com.jcondotta.exception.ResourceNotFoundException;
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
public class BankAccountNotFoundExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountNotFoundExceptionHandler.class);

    private final LocaleResolverPort localeResolverPort;
    private final MessageResolverPort messageResolverPort;
    private final Clock clock;

    public BankAccountNotFoundExceptionHandler(@Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort,
                                               MessageResolverPort messageResolverPort,
                                               Clock clock) {
        this.localeResolverPort = localeResolverPort;
        this.messageResolverPort = messageResolverPort;
        this.clock = clock;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleBankAccountNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        LOGGER.warn("Bank account not found: {}", ex.getMessage());

        var locale = localeResolverPort.resolveLocale();
        var messageArgs = new Object[]{ex.getIdentifier()};
        var message = messageResolverPort.resolveMessage(ex.getMessage(), messageArgs, locale);

        var problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withTitle("Bank Account Not Found")
                .withDetail(message)
                .with("timestamp", Instant.now(clock))
                .build();

        return ResponseEntity.status(Status.NOT_FOUND.getStatusCode()).body(problem);
    }
}
