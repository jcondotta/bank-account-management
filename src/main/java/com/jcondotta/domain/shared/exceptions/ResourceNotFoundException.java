package com.jcondotta.domain.shared.exceptions;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final Serializable[] identifiers;

    public ResourceNotFoundException(String message, Serializable... identifiers) {
        super(message);
        this.identifiers = identifiers;
    }

    public ResourceNotFoundException(String message, Throwable cause, Serializable... identifiers) {
        super(message, cause);
        this.identifiers = identifiers;
    }
}
