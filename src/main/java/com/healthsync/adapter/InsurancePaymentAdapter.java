package com.healthsync.adapter;
/** Adapts an insurance claim API to HealthSync's unified Payment contract. */
public class InsurancePaymentAdapter implements Payment {
    private final InsuranceProvider provider;
    public InsurancePaymentAdapter(InsuranceProvider provider) { this.provider=provider; }
    public PaymentResult process(PaymentRequest request) { String claimReference=provider.submitClaim(request.invoiceId(), request.amount()); return new PaymentResult(claimReference != null && !claimReference.isBlank(), claimReference, "Insurance claim submitted"); }
}
