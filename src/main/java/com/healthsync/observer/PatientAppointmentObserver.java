package com.healthsync.observer;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.service.NotificationService;
/** Notifies the patient recipient after a lifecycle transition. */
public class PatientAppointmentObserver implements AppointmentStatusObserver {
    private final NotificationService notifications;
    public PatientAppointmentObserver(NotificationService notifications) { this.notifications=notifications; }
    public void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus) { notifications.notifyRecipient(appointment.getPatientId(), "Appointment " + appointment.getId() + " is now " + appointment.getStatus()); }
}
