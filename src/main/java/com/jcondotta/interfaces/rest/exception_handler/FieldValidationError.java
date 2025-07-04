package com.jcondotta.interfaces.rest.exception_handler;

import java.util.List;

public record FieldValidationError(String field, List<String> messages) {}
