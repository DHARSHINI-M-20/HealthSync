package com.healthsync.abstractfactory;

import com.healthsync.model.Notification;
import java.time.Instant;

/** Hospital notification product, categorized for hospital workflow/auditing. */
public final class HospitalNotification implements HealthcareNotification {
    public Notification create(String id, String recipientId, String message, Instant createdAt) { return new Notification(id, recipientId, "HOSPITAL_SERVICE", message, "PENDING", createdAt); }
    public String environmentName() { return "HOSPITAL"; }
}
