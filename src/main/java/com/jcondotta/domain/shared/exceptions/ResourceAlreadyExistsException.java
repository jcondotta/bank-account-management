package com.jcondotta.domain.shared.exceptions;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {

    private final Serializable identifier;

    public ResourceAlreadyExistsException(String message, Serializable identifier) {
        super(message);
        this.identifier = identifier;
    }

    public ResourceAlreadyExistsException(String message, Throwable cause, Serializable identifier) {
        super(message, cause);
        this.identifier = identifier;
    }
}
