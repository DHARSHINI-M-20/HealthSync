package com.healthsync.state;
import com.healthsync.model.AppointmentStatus;
/** Terminal state for a completed consultation. */
public class CompletedState extends AbstractAppointmentState { public AppointmentStatus status() { return AppointmentStatus.COMPLETED; } }
