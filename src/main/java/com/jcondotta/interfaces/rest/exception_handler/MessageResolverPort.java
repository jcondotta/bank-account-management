package com.jcondotta.interfaces.rest.exception_handler;

import java.util.Locale;

@FunctionalInterface
public interface MessageResolverPort {

    String resolveMessage(String messageCode, Object[] args, Locale locale);

    default String resolveMessage(String messageCode, Locale locale){
        return resolveMessage(messageCode, null, locale);
    }
}