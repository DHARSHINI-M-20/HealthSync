package com.healthsync.service;

import com.healthsync.model.Appointment;
import com.healthsync.observer.*;
import com.healthsync.repository.AppointmentRepository;
import com.healthsync.state.RequestedState;
import java.util.List;
import java.util.UUID;

/** Coordinates state transitions, observer attachment, priority strategies, and repository persistence. */
public class AppointmentManagementService implements AppointmentService {
    private final AppointmentRepository appointments;
    private final AppointmentStatusObserver patientObserver;
    private final AppointmentStatusObserver doctorObserver;
    private final AppointmentStatusObserver notificationObserver;

    public AppointmentManagementService(AppointmentRepository appointments, NotificationService notifications) {
        this.appointments=appointments;
        patientObserver=new PatientAppointmentObserver(notifications);
        doctorObserver=new DoctorAppointmentObserver(notifications);
        notificationObserver=new NotificationServiceAppointmentObserver(notifications);
    }

    public Appointment book(AppointmentBookingRequest request) {
        validateBooking(request);
        Appointment appointment=new Appointment(UUID.randomUUID().toString(), request.patientId(), request.doctorId(), request.scheduledAt(), new RequestedState(), request.priorityStrategy().type(), request.priorityStrategy().priorityScore());
        return appointments.save(appointment);
    }
    public Appointment cancel(String appointmentId) { return transition(appointmentId, Appointment::cancel); }
    public Appointment confirm(String appointmentId) { return transition(appointmentId, Appointment::confirm); }
    public Appointment complete(String appointmentId) { return transition(appointmentId, Appointment::complete); }
    public Appointment checkIn(String appointmentId) { return transition(appointmentId, Appointment::checkIn); }
    public Appointment startConsultation(String appointmentId) { return transition(appointmentId, Appointment::startConsultation); }
    public Appointment reject(String appointmentId) { return transition(appointmentId, Appointment::reject); }
    public List<Appointment> findAll() { return appointments.findAll(); }

    private Appointment transition(String appointmentId, java.util.function.Consumer<Appointment> action) {
        Appointment appointment=appointments.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));
        appointment.addObserver(patientObserver); appointment.addObserver(doctorObserver); appointment.addObserver(notificationObserver);
        action.accept(appointment);
        return appointments.save(appointment);
    }
    private void validateBooking(AppointmentBookingRequest request) {
        if (request == null || request.patientId() == null || request.patientId().isBlank() || request.doctorId() == null || request.doctorId().isBlank() || request.scheduledAt() == null || request.priorityStrategy() == null) throw new IllegalArgumentException("Patient, doctor, date, and priority strategy are required");
    }
}
