package com.healthsync.adapter;
import java.math.BigDecimal;
/** Example shape of an external insurance claims API. */
public interface InsuranceProvider { String submitClaim(String invoiceNumber, BigDecimal requestedAmount); }
