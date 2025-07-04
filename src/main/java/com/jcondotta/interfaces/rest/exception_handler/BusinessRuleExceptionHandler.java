package com.jcondotta.interfaces.rest.exception_handler;

import com.jcondotta.domain.shared.exceptions.BusinessRuleException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;

@ControllerAdvice
public class BusinessRuleExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessRuleExceptionHandler.class);

    private final LocaleResolverPort localeResolverPort;
    private final MessageResolverPort messageResolverPort;
    private final Clock clock;

    public BusinessRuleExceptionHandler(@Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort,
                                        MessageResolverPort messageResolverPort,
                                        Clock clock) {
        this.localeResolverPort = localeResolverPort;
        this.messageResolverPort = messageResolverPort;
        this.clock = clock;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ProblemDetail> handleBusinessRuleException(BusinessRuleException ex, HttpServletRequest request) {
        var locale = localeResolverPort.resolveLocale();
        var message = messageResolverPort.resolveMessage(ex.getMessage(), ex.getProperties().values().toArray(new Object[0]), locale);

        LOGGER.warn("Domain rule violated: {}", message);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problem.setTitle(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        problem.setDetail(message);
        problem.setType(URI.create(ex.getType()));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now(clock));
        problem.setProperties(ex.getProperties());

        return ResponseEntity.unprocessableEntity().body(problem);
    }
}
