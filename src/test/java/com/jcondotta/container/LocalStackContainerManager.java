package com.jcondotta.container;

import org.testcontainers.lifecycle.Startables;

public class LocalStackContainerManager {

    private static LocalStackContainerManager instance;
    private boolean started = false;

    private LocalStackContainerManager() {}

    public static synchronized LocalStackContainerManager getInstance() {
        if (instance == null) {
            instance = new LocalStackContainerManager();
        }
        return instance;
    }

    public synchronized void startContainer() {
        if (!started) {
            try {
                Startables.deepStart(LocalStackTestContainer.LOCALSTACK_CONTAINER).join();
                started = true;
            } catch (Exception e) {
                throw new RuntimeException("Failed to start LocalStack container", e);
            }
        }
    }
}