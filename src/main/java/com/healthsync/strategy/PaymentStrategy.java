package com.healthsync.strategy;
import com.healthsync.model.Invoice;
/** Varying payment method algorithm. */
public interface PaymentStrategy { boolean pay(Invoice invoice); }
