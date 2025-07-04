package com.jcondotta.interfaces.rest.exception_handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetails {

    private final String type;
    private final String title;
    private final int status;
    private final Instant timestamp;
    private final String instance;
    private final Map<String, Object> properties;

    public ProblemDetails(String type, String title, int status, Instant timestamp, String instance, Map<String, Object> properties) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.timestamp = timestamp;
        this.instance = instance;
        this.properties = properties;
    }
}
