package com.healthsync.state;
import com.healthsync.model.AppointmentStatus;
/** Rehydrates State objects from persisted status values. */
public final class AppointmentStates {
    private AppointmentStates() { }
    public static AppointmentState from(AppointmentStatus status) { return switch (status) { case REQUESTED -> new RequestedState(); case CONFIRMED -> new ConfirmedState(); case CHECKED_IN -> new CheckedInState(); case IN_CONSULTATION -> new InConsultationState(); case COMPLETED -> new CompletedState(); case CANCELLED -> new CancelledState(); case REJECTED -> new RejectedState(); }; }
}
