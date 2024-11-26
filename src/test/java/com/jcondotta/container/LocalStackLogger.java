package com.jcondotta.container;

import org.slf4j.Logger;

import java.util.Map;

public class LocalStackLogger {

    public static void logContainerConfiguration(Logger logger, Map<String, String> containerProperties) {
        var logBuilder = new StringBuilder();
        logBuilder.append("\n================== Container Configuration ==================\n");

        containerProperties.forEach((key, value) ->
                logBuilder.append(String.format("  %s : %s%n", key, value))
        );

        logBuilder.append("=============================================================\n");

        logger.info(logBuilder.toString());
    }
}