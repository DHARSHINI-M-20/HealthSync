package com.healthsync.adapter;
/** HealthSync's single internal payment contract, independent of vendor APIs. */
public interface Payment { PaymentResult process(PaymentRequest request); }
