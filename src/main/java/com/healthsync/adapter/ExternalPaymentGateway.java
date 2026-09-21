package com.healthsync.adapter;
import java.math.BigDecimal;
/** Example shape of a third-party card/UPI gateway SDK. */
public interface ExternalPaymentGateway { boolean submitCharge(String merchantReference, BigDecimal amount); }
