package com.healthsync.service;
import com.healthsync.model.Invoice;
import com.healthsync.model.Payment;
import java.math.BigDecimal;
import java.util.List;
public interface BillingService { Invoice issueInvoice(String appointmentId); Payment recordPayment(String invoiceId, BigDecimal amount, String method); List<Payment> findPayments(String invoiceId); List<Invoice> findAllInvoices(); }
