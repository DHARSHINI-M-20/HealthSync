package com.healthsync.state;
import com.healthsync.model.AppointmentStatus;
/** Terminal state for requests declined by the provider or administrator. */
public class RejectedState extends AbstractAppointmentState { public AppointmentStatus status() { return AppointmentStatus.REJECTED; } }
