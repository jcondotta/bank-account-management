package com.jcondotta.web.controller.exception_handler;

import java.util.Locale;

@FunctionalInterface
public interface LocaleResolverPort {
    Locale resolveLocale();
}
