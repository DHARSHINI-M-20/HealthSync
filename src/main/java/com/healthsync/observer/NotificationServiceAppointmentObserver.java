package com.healthsync.observer;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.service.NotificationService;
/** Lets the notification service record or fan out the lifecycle event. */
public class NotificationServiceAppointmentObserver implements AppointmentStatusObserver {
    private final NotificationService notifications;
    public NotificationServiceAppointmentObserver(NotificationService notifications) { this.notifications=notifications; }
    public void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus) { notifications.notifyAppointmentStatus(appointment, previousStatus); }
}
