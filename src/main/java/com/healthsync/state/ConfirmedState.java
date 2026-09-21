package com.healthsync.state;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
public class ConfirmedState extends AbstractAppointmentState {
    public void checkIn(Appointment appointment) { appointment.transitionTo(new CheckedInState()); }
    public void cancel(Appointment appointment) { appointment.transitionTo(new CancelledState()); }
    public AppointmentStatus status() { return AppointmentStatus.CONFIRMED; }
}
