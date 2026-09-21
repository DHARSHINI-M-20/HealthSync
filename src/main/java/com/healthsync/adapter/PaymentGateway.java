package com.healthsync.adapter;
import java.math.BigDecimal;
/** HealthSync's payment-provider boundary. */
public interface PaymentGateway { boolean charge(String reference, BigDecimal amount); }
