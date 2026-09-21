package com.healthsync.repository;
import com.healthsync.model.Payment;
import java.util.List;
/** Persistence boundary for invoice payment transactions. */
public interface PaymentRepository extends Repository<Payment> { List<Payment> findByInvoiceId(String invoiceId); }
