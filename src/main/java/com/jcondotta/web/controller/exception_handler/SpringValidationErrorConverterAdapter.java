package com.jcondotta.web.controller.exception_handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SpringValidationErrorConverterAdapter implements ValidationErrorConverterPort<FieldError, FieldValidationError> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringValidationErrorConverterAdapter.class);

    private final MessageResolverPort messageResolverPort;
    private final LocaleResolverPort localeResolverPort;

    public SpringValidationErrorConverterAdapter(MessageResolverPort messageResolverPort,
                                                 @Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort) {
        this.messageResolverPort = messageResolverPort;
        this.localeResolverPort = localeResolverPort;
    }

    @Override
    public List<FieldValidationError> apply(Collection<FieldError> fieldErrors) {
        Locale userLocale = localeResolverPort.resolveLocale();
        LOGGER.debug("Resolving validation messages with locale: {}", userLocale);

        Map<String, List<String>> fieldToMessagesMap = fieldErrors.stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(fieldError -> resolveValidationMessage(fieldError, userLocale),
                                Collectors.toList())
                ));

        return fieldToMessagesMap.entrySet()
                .stream()
                .map(entry -> new FieldValidationError(entry.getKey(), entry.getValue()))
                .toList();
    }

    private String resolveValidationMessage(FieldError fieldError, Locale locale) {
        String messageCode = fieldError.getDefaultMessage();
        String resolvedMessage = messageResolverPort.resolveMessage(messageCode, locale);

        if (resolvedMessage.equals(messageCode)) {
            LOGGER.warn("Message code '{}' not found for locale '{}', using default.", messageCode, locale);
        }

        return resolvedMessage;
    }
}
