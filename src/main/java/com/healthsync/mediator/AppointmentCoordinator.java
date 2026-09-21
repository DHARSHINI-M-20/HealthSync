package com.healthsync.mediator;
import com.healthsync.model.Appointment;
/** Mediates appointment scheduling collaborators. */
public interface AppointmentCoordinator { void coordinateBooking(Appointment appointment); }
