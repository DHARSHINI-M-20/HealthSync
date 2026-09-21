package com.healthsync.state;
import com.healthsync.model.Appointment;
/** Base state rejects transitions that are not valid in the current lifecycle position. */
public abstract class AbstractAppointmentState implements AppointmentState {
    public void confirm(Appointment appointment) { invalid("confirm"); }
    public void checkIn(Appointment appointment) { invalid("check in"); }
    public void startConsultation(Appointment appointment) { invalid("start consultation"); }
    public void complete(Appointment appointment) { invalid("complete"); }
    public void cancel(Appointment appointment) { invalid("cancel"); }
    public void reject(Appointment appointment) { invalid("reject"); }
    protected void invalid(String action) { throw new IllegalStateException("Cannot " + action + " an appointment in this state"); }
}
