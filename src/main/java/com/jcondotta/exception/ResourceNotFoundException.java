package com.jcondotta.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final Serializable identifier;

    public ResourceNotFoundException(Serializable identifier, String message) {
        super(message);
        this.identifier = identifier;
    }
}
