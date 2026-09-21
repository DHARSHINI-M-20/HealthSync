package com.healthsync.facade;

import com.healthsync.adapter.Payment;
import com.healthsync.adapter.PaymentRequest;
import com.healthsync.adapter.PaymentResult;
import com.healthsync.model.Appointment;
import com.healthsync.model.MedicalRecord;
import com.healthsync.service.*;

/** Single entry point for common multi-service healthcare workflows. */
public class HealthcareFacade {
    private final AppointmentService appointments;
    private final MedicalRecordService records;
    private final Payment payments;
    private final NotificationService notifications;
    public HealthcareFacade(AppointmentService appointments, MedicalRecordService records, Payment payments, NotificationService notifications) { this.appointments=appointments; this.records=records; this.payments=payments; this.notifications=notifications; }
    public Appointment bookAppointment(AppointmentBookingRequest request) { return appointments.book(request); }
    public MedicalRecord createMedicalRecord(MedicalRecord record) { return records.save(record); }
    public void completeConsultation(String appointmentId, MedicalRecord record) { records.save(record); appointments.complete(appointmentId); }
    public PaymentResult processPayment(PaymentRequest request) { return payments.process(request); }
    public void sendNotification(String recipientId, String message) { notifications.notifyRecipient(recipientId, message); }
}
