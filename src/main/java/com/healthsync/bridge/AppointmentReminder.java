package com.healthsync.bridge;
public class AppointmentReminder extends Notification {
    public AppointmentReminder(NotificationChannel channel) { super(channel); }
    public void send(String recipient) { channel.deliver(recipient, "Appointment reminder", "TODO: Compose appointment reminder."); }
}
