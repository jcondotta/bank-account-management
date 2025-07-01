package com.jcondotta.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "openapi")
public class OpenAPIProperties {

    private String version;
    private String title;
    private String description;
    private Map<String, String> contact;
    private Map<String, String> license;
    private String termsOfService;
    private List<Map<String, String>> serverUrls;
    private List<Map<String, String>> tags;
    private Map<String, String> security;
    private Map<String, String> rateLimits;
}
