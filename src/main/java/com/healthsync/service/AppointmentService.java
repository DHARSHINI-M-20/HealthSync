package com.healthsync.service;
import com.healthsync.model.Appointment;
import java.util.List;
/** Application boundary for command-driven appointment use cases. */
public interface AppointmentService {
    Appointment book(AppointmentBookingRequest request);
    Appointment cancel(String appointmentId);
    Appointment confirm(String appointmentId);
    Appointment complete(String appointmentId);
    Appointment checkIn(String appointmentId);
    Appointment startConsultation(String appointmentId);
    Appointment reject(String appointmentId);
    List<Appointment> findAll();
}
