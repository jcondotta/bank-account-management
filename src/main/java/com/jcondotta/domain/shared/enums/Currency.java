package com.jcondotta.domain.shared.enums;

public enum Currency {
    EUR("€"), USD("$");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
}
