package com.healthsync.mediator;

import com.healthsync.model.Appointment;
import com.healthsync.service.NotificationService;
import com.healthsync.service.AppointmentService;
import com.healthsync.service.BillingService;
import com.healthsync.adapter.PaymentRequest;
import com.healthsync.adapter.PaymentResult;
import java.math.BigDecimal;

/** Coordinates scheduling, resource allocation, billing, and notification. */
public class HealthcareAppointmentMediator implements AppointmentCoordinator {
    private final NotificationService notifications;
    private final AppointmentService appointments;
    private final BillingService billing;
    public HealthcareAppointmentMediator(NotificationService notifications) { this(notifications, null, null); }
    public HealthcareAppointmentMediator(NotificationService notifications, AppointmentService appointments, BillingService billing) {
        this.notifications = notifications; this.appointments = appointments; this.billing = billing;
    }
    @Override public void coordinateBooking(Appointment appointment) {
        if (appointment == null) throw new IllegalArgumentException("Appointment is required");
        if (notifications != null) notifications.notifyRecipient(appointment.getPatientId(),
                "Appointment " + appointment.getId() + " scheduled for " + appointment.getScheduledAt());
        if (billing != null) {
            billing.issueInvoice(appointment.getId());
        }
    }
}