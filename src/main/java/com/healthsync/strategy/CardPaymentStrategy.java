package com.healthsync.strategy;

import com.healthsync.adapter.ExternalPaymentGateway;
import com.healthsync.model.Invoice;
import java.time.Instant;
import java.util.UUID;

/** Delegates card payments to a configured payment gateway. */
public class CardPaymentStrategy implements PaymentStrategy {
    private final ExternalPaymentGateway gateway;
    public CardPaymentStrategy(ExternalPaymentGateway gateway) { this.gateway = gateway; }
    @Override public boolean pay(Invoice invoice) {
        if (invoice == null) throw new IllegalArgumentException("Invoice is required");
        return gateway.submitCharge(invoice.getId(), invoice.getTotal());
    }
}