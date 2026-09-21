package com.healthsync.interpreter;

import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;

/** Matches an appointment by its current status. */
public class StatusExpression implements ReportExpression {
    private final AppointmentStatus status;
    public StatusExpression(AppointmentStatus status) { this.status = status; }
    @Override public boolean interpret(Appointment appointment) { return appointment.getStatus() == status; }
}