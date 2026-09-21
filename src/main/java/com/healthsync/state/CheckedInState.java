package com.healthsync.state;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
/** Patient has arrived and may enter consultation. */
public class CheckedInState extends AbstractAppointmentState {
    public void startConsultation(Appointment appointment) { appointment.transitionTo(new InConsultationState()); }
    public void cancel(Appointment appointment) { appointment.transitionTo(new CancelledState()); }
    public AppointmentStatus status() { return AppointmentStatus.CHECKED_IN; }
}
