package com.healthsync.adapter;
import java.math.BigDecimal;
/** Vendor-neutral payment/claim request. */
public record PaymentRequest(String invoiceId, BigDecimal amount, String reference) { }
