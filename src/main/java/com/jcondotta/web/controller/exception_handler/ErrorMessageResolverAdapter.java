package com.jcondotta.web.controller.exception_handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ErrorMessageResolverAdapter implements com.jcondotta.web.controller.exception_handler.MessageResolverPort {

    private final MessageSource errorMessageSource;

    public ErrorMessageResolverAdapter(@Qualifier("errorMessageSource") MessageSource errorMessageSource) {
        this.errorMessageSource = errorMessageSource;
    }

    @Override
    public String resolveMessage(String messageCode, Object[] args, Locale locale) {
        return errorMessageSource.getMessage(messageCode, args, messageCode, locale);
    }
}
