package com.healthsync.state;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
/** Consultation is in progress; only completion is a valid terminal transition. */
public class InConsultationState extends AbstractAppointmentState {
    public void complete(Appointment appointment) { appointment.transitionTo(new CompletedState()); }
    public AppointmentStatus status() { return AppointmentStatus.IN_CONSULTATION; }
}
