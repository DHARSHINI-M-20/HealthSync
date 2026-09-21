package com.healthsync.state;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
/** State for an appointment pending approval. */
public class RequestedState extends AbstractAppointmentState {
    public void confirm(Appointment appointment) { appointment.transitionTo(new ConfirmedState()); }
    public void cancel(Appointment appointment) { appointment.transitionTo(new CancelledState()); }
    public void reject(Appointment appointment) { appointment.transitionTo(new RejectedState()); }
    public AppointmentStatus status() { return AppointmentStatus.REQUESTED; }
}
