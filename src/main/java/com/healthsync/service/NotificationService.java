package com.healthsync.service;
import com.healthsync.bridge.Notification;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import java.util.List;
public interface NotificationService {
    void send(Notification notification);
    void notifyRecipient(String recipientId, String message);
    void notifyAppointmentStatus(Appointment appointment, AppointmentStatus previousStatus);
    List<com.healthsync.model.Notification> findAll();
}
