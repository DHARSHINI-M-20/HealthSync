package com.healthsync.bridge;
/** Notification abstraction independent of delivery channel. */
public abstract class Notification {
    protected final NotificationChannel channel;
    protected Notification(NotificationChannel channel) { this.channel = channel; }
    public abstract void send(String recipient);
}
