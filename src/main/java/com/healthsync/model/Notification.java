package com.healthsync.model;

import java.time.Instant;

/** Persisted notification delivery record, distinct from the bridge abstraction. */
public class Notification {
    private final String id;
    private final String recipientId;
    private final String type;
    private final String message;
    private final String status;
    private final Instant createdAt;

    public Notification(String id, String recipientId, String type, String message, String status, Instant createdAt) {
        this.id=id; this.recipientId=recipientId; this.type=type; this.message=message; this.status=status; this.createdAt=createdAt;
    }
    public String getId() { return id; }
    public String getRecipientId() { return recipientId; }
    public String getType() { return type; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
