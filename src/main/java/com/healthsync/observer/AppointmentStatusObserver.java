package com.healthsync.observer;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
/** Receives appointment lifecycle changes from an Appointment subject. */
public interface AppointmentStatusObserver { void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus); }
