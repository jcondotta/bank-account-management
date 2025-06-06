package com.jcondotta.event;

import java.util.UUID;

public record AccountHolderCreatedNotification(UUID accountHolderId, String accountHolderName, UUID bankAccountId) {}