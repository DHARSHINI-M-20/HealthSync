package com.healthsync.chain;
import com.healthsync.model.Appointment;
/** Handler in appointment validation and approval pipeline. */
public abstract class AppointmentRequestHandler {
    private AppointmentRequestHandler next;
    public AppointmentRequestHandler linkWith(AppointmentRequestHandler next) { this.next=next; return next; }
    public final boolean handle(Appointment appointment) { return check(appointment) && (next == null || next.handle(appointment)); }
    protected abstract boolean check(Appointment appointment);
}
