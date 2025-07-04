package com.jcondotta.interfaces.rest.exception_handler;

import java.util.Locale;

@FunctionalInterface
public interface LocaleResolverPort {
    Locale resolveLocale();
}
