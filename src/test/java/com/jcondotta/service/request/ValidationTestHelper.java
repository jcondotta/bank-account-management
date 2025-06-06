package com.jcondotta.service.request;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationTestHelper {

    private ValidationTestHelper() {}

    public static void assertSingleViolation(Set<ConstraintViolation<?>> violations,
                                                 String expectedMessage,
                                                 String expectedPropertyPath) {
        assertThat(violations)
                .as("Expecting exactly one constraint violation")
                .hasSize(1)
                .singleElement()
                .extracting(ConstraintViolation::getMessage, v -> v.getPropertyPath().toString())
                .containsExactly(expectedMessage, expectedPropertyPath);
    }
}
