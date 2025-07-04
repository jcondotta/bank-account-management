package com.jcondotta.interfaces.rest.exception_handler;

import java.util.Collection;
import java.util.function.Function;

@FunctionalInterface
public interface ValidationErrorConverterPort<T, R> extends Function<Collection<T>, Collection<R>> {

}
