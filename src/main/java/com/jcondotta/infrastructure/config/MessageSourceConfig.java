package com.jcondotta.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceConfig {

    @Bean(name = "errorMessageSource")
    public org.springframework.context.support.ResourceBundleMessageSource errorMessageSource() {
        var messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename("i18n/exceptions/exceptions");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(false);
        messageSource.setDefaultLocale(Locale.ENGLISH);

        return messageSource;
    }
}