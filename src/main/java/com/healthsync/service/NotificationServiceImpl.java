package com.healthsync.service;

import com.healthsync.bridge.NotificationChannel;
import com.healthsync.bridge.EmailChannel;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.model.Notification;
import com.healthsync.repository.NotificationRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Persists notifications and dispatches them through pluggable delivery channels. */
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationChannel channel;
    public NotificationServiceImpl(NotificationRepository repository) { this(repository, new EmailChannel()); }
    public NotificationServiceImpl(NotificationRepository repository, NotificationChannel channel) {
        this.repository = repository; this.channel = channel;
    }
    @Override public void send(com.healthsync.bridge.Notification notification) { repository.save(toModel(notification)); }
    @Override public void notifyRecipient(String recipientId, String message) {
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Message is required");
        channel.deliver(recipientId, "HealthSync", message);
        repository.save(new Notification(UUID.randomUUID().toString(), recipientId.trim(), "EMAIL", message, "SENT", Instant.now()));
    }
    @Override public void notifyAppointmentStatus(Appointment appointment, AppointmentStatus previousStatus) {
        String message = "Appointment " + appointment.getId() + " moved from " + previousStatus + " to " + appointment.getStatus();
        notifyRecipient(appointment.getPatientId(), message);
    }
    @Override public List<com.healthsync.model.Notification> findAll() { return repository.findAll(); }
    private com.healthsync.model.Notification toModel(com.healthsync.bridge.Notification bridge) {
        return new com.healthsync.model.Notification(UUID.randomUUID().toString(), "UNKNOWN", "BRIDGE", bridge.toString(), "PENDING", Instant.now());
    }
}