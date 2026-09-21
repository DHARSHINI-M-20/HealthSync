package com.healthsync.strategy;

import com.healthsync.model.Invoice;
import com.healthsync.model.Payment;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Records a verified cash payment against an invoice. */
public class CashPaymentStrategy implements PaymentStrategy {
    private final java.util.function.BiFunction<String, BigDecimal, Payment> recorder;
    public CashPaymentStrategy() { this((id, amount) -> new Payment(UUID.randomUUID().toString(), id, amount, "CASH", "COMPLETED", Instant.now())); }
    public CashPaymentStrategy(java.util.function.BiFunction<String, BigDecimal, Payment> recorder) { this.recorder = recorder; }
    @Override public boolean pay(Invoice invoice) {
        if (invoice == null) throw new IllegalArgumentException("Invoice is required");
        recorder.apply(invoice.getId(), invoice.getTotal());
        return true;
    }
}