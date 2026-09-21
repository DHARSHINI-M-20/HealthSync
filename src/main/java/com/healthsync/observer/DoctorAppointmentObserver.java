package com.healthsync.observer;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.service.NotificationService;
/** Notifies the assigned doctor after a lifecycle transition. */
public class DoctorAppointmentObserver implements AppointmentStatusObserver {
    private final NotificationService notifications;
    public DoctorAppointmentObserver(NotificationService notifications) { this.notifications=notifications; }
    public void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus) { notifications.notifyRecipient(appointment.getDoctorId(), "Appointment " + appointment.getId() + " is now " + appointment.getStatus()); }
}
