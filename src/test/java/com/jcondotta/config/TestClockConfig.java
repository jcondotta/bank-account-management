package com.jcondotta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Configuration
public class TestClockConfig {

    public static final Clock testClockFixedInstant = Clock.fixed(Instant.parse("2022-06-24T12:45:01Z"), ZoneOffset.UTC);

    @Bean
    @Primary
    public Clock testClockFixedInstant(){
        return testClockFixedInstant;
    }
}