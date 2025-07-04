package com.jcondotta.domain.accountholder.valueobjects;

public record PassportNumber(String value) {

    public static final int LENGTH = 8;

//    public PassportNumber {
//        String normalized = normalize(value);
//        validate(normalized);
//        this.value = normalized;
//    }

    public static PassportNumber of(String value) {
        return new PassportNumber(value);
    }

    private static String normalize(String input) {
        return input == null ? null : input.trim().toUpperCase();
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Passport number must not be blank.");
        }

        if (value.length() != LENGTH) {
            throw new IllegalArgumentException("Passport number must be exactly " + LENGTH + " characters.");
        }

        if (!value.matches("^[A-Z0-9]+$")) {
            throw new IllegalArgumentException("Passport number must contain only letters and digits.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
