package com.healthsync.state;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
public class CancelledState extends AbstractAppointmentState {
    public AppointmentStatus status() { return AppointmentStatus.CANCELLED; }
}
