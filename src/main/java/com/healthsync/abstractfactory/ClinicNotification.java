package com.healthsync.abstractfactory;

import com.healthsync.model.Notification;
import java.time.Instant;

/** Clinic notification product, categorized for clinic workflow/auditing. */
public final class ClinicNotification implements HealthcareNotification {
    public Notification create(String id, String recipientId, String message, Instant createdAt) { return new Notification(id, recipientId, "CLINIC_SERVICE", message, "PENDING", createdAt); }
    public String environmentName() { return "CLINIC"; }
}
