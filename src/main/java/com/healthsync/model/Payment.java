package com.healthsync.model;

import java.math.BigDecimal;
import java.time.Instant;

/** A recorded payment against an invoice. */
public class Payment {
    private final String id;
    private final String invoiceId;
    private final BigDecimal amount;
    private final String method;
    private final String status;
    private final Instant paidAt;

    public Payment(String id, String invoiceId, BigDecimal amount, String method, String status, Instant paidAt) {
        this.id = id; this.invoiceId = invoiceId; this.amount = amount; this.method = method; this.status = status; this.paidAt = paidAt;
    }
    public String getId() { return id; }
    public String getInvoiceId() { return invoiceId; }
    public BigDecimal getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public Instant getPaidAt() { return paidAt; }
}
