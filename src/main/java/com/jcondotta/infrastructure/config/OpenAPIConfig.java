package com.jcondotta.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Profile({"dev", "staging"})
@PropertySource("classpath:openapi.properties")
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class OpenAPIConfig {

    private final OpenAPIProperties properties;

    public OpenAPIConfig(OpenAPIProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .description(properties.getDescription())
                        .termsOfService(properties.getTermsOfService())
//                        .contact(new Contact()
//                                .name(properties.getContact().get("name"))
//                                .email(properties.getContact().get("email"))
//                                .url(properties.getContact().get("url"))
//                        )
//                        .license(new License()
//                                .name(properties.getLicense().get("name"))
//                                .url(properties.getLicense().get("url"))
//                        )
                );
//                .servers(loadServers())
//                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
//                .components(new Components()
//                        .addSecuritySchemes("BearerAuth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme(properties.getSecurity().get("scheme"))
//                                        .bearerFormat(properties.getSecurity().get("type"))
//                                        .description(properties.getSecurity().get("description"))
//                        )
//                )
//                .tags(loadTags());
    }

    private List<Server> loadServers() {
        return properties.getServerUrls().stream()
                .map(server -> new Server().url(server.get("url")).description(server.get("description")))
                .collect(Collectors.toList());
    }

    private List<Tag> loadTags() {
        return properties.getTags().stream()
                .map(tag -> new Tag().name(tag.get("name")).description(tag.get("description")))
                .collect(Collectors.toList());
    }
}
