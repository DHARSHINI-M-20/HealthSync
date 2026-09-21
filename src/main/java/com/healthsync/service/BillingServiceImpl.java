package com.healthsync.service;

import com.healthsync.model.Appointment;
import com.healthsync.model.Invoice;
import com.healthsync.model.Payment;
import com.healthsync.repository.AppointmentRepository;
import com.healthsync.repository.InvoiceRepository;
import com.healthsync.repository.PaymentRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Issues invoices and records payments against completed appointments. */
public class BillingServiceImpl implements BillingService {
    private final PaymentRepository payments;
    private final AppointmentRepository appointments;
    private final InvoiceRepository invoices;
    public BillingServiceImpl(PaymentRepository payments) { this(payments, null, null); }
    public BillingServiceImpl(PaymentRepository payments, AppointmentRepository appointments, InvoiceRepository invoices) {
        this.payments = payments; this.appointments = appointments; this.invoices = invoices;
    }
    @Override public Invoice issueInvoice(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) throw new IllegalArgumentException("Appointment ID is required");
        Appointment appointment = Optional.ofNullable(appointments)
                .flatMap(repository -> repository.findById(appointmentId))
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), appointment.getPatientId(), appointmentId, new BigDecimal("50.00"));
        if (invoices != null) invoices.save(invoice);
        return invoice;
    }
    @Override public Payment recordPayment(String invoiceId, BigDecimal amount, String method) {
        if (invoiceId == null || invoiceId.isBlank()) throw new IllegalArgumentException("Invoice ID is required");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Payment amount must be positive");
        if (method == null || method.isBlank()) throw new IllegalArgumentException("Payment method is required");
        if (invoices != null && invoices.findById(invoiceId).isEmpty()) throw new IllegalArgumentException("Invoice not found: " + invoiceId);
        Payment payment = new Payment(UUID.randomUUID().toString(), invoiceId, amount, method, "COMPLETED", Instant.now());
        return payments.save(payment);
    }
    @Override public List<Payment> findPayments(String invoiceId) { return payments.findByInvoiceId(invoiceId); }
    @Override public List<Invoice> findAllInvoices() { return invoices == null ? List.of() : invoices.findAll(); }
}