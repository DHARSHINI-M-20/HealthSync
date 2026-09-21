package com.healthsync.adapter;
import java.math.BigDecimal;
/** Adapts a card/UPI gateway to both legacy gateway and unified Payment contracts. */
public class PaymentGatewayAdapter implements PaymentGateway, Payment {
    private final ExternalPaymentGateway gateway;
    public PaymentGatewayAdapter(ExternalPaymentGateway gateway) { this.gateway=gateway; }
    public boolean charge(String reference, BigDecimal amount) { return gateway.submitCharge(reference, amount); }
    public PaymentResult process(PaymentRequest request) { boolean success=charge(request.reference(), request.amount()); return new PaymentResult(success, request.reference(), success ? "Payment approved" : "Payment declined"); }
}
