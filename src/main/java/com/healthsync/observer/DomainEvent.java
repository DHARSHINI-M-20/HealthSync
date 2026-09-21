package com.healthsync.observer;
import java.time.Instant;
/** Business event published after meaningful domain changes. */
public record DomainEvent(String type, String entityId, Instant occurredAt) { }
