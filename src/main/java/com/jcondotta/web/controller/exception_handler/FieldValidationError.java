package com.jcondotta.web.controller.exception_handler;

import java.util.List;

public record FieldValidationError(String field, List<String> messages) {}
