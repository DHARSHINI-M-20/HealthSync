package com.healthsync.abstractfactory;

import com.healthsync.model.Notification;
import java.time.Instant;

/** Abstract notification product for environment-specific message classification. */
public interface HealthcareNotification {
    Notification create(String id, String recipientId, String message, Instant createdAt);
    String environmentName();
}
