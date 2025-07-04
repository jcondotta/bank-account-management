package com.jcondotta.interfaces.rest.exception_handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;
import java.util.Optional;

@Component("httpRequestLocaleResolver")
public class HttpRequestLocaleResolverAdapter implements LocaleResolverPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestLocaleResolverAdapter.class);

    private final LocaleResolver localeResolver;
    private final Locale defaultLocale;

    public HttpRequestLocaleResolverAdapter(
            LocaleResolver localeResolver, @Value("${spring.application.locale.default:en}") String defaultLocaleCode) {
        this.localeResolver = localeResolver;
        this.defaultLocale = Locale.forLanguageTag(defaultLocaleCode);
    }

    @Override
    public Locale resolveLocale() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .map(request -> {
                    String acceptLanguage = request.getHeader("Accept-Language");
                    Locale resolvedLocale = localeResolver.resolveLocale(request);

                    LOGGER.info("Resolved locale: {} (Accept-Language: {})", resolvedLocale, acceptLanguage);
                    return resolvedLocale;
                })
                .orElseGet(() -> {
                    LOGGER.warn("No request context found. Defaulting to: {}", defaultLocale);
                    return defaultLocale;
                });
    }
}
