package com.jcondotta.web.controller.exception_handler;

import com.jcondotta.exception.BankAccountNotFoundException;
import io.micronaut.context.MessageSource;
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

import java.util.Locale;

@Produces
@Singleton
@Requires(classes = { BankAccountNotFoundException.class })
public class BankAccountNotFoundExceptionHandler implements ExceptionHandler<BankAccountNotFoundException, HttpResponse<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountNotFoundExceptionHandler.class);

    private final MessageSource messageSource;
    private final ErrorResponseProcessor<?> errorResponseProcessor;

    @Inject
    public BankAccountNotFoundExceptionHandler(MessageSource messageSource, ErrorResponseProcessor<?> errorResponseProcessor) {
        this.messageSource = messageSource;
        this.errorResponseProcessor = errorResponseProcessor;
    }

    @Override
    @Status(value = HttpStatus.NOT_FOUND)
    public HttpResponse<?> handle(HttpRequest request, BankAccountNotFoundException exception) {
        var errorMessage = messageSource.getMessage(exception.getMessage(), Locale.getDefault(), exception.getBankAccountId())
                .orElse(exception.getMessage());

        LOGGER.error(errorMessage);

        return errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(exception)
                .errorMessage(errorMessage)
                .build(), HttpResponse.notFound());
    }
}