package com.healthsync.adapter;
/** Normalized result returned regardless of payment or insurance provider. */
public record PaymentResult(boolean successful, String transactionReference, String message) { }
