package com.healthsync.bridge;
/** Delivery implementor in the Notification bridge. */
public interface NotificationChannel { void deliver(String recipient, String subject, String body); }
