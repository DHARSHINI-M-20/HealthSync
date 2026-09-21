package com.healthsync.decorator;
import java.math.BigDecimal;
/** Bill component supporting dynamic clinical charges and adjustments. */
public interface BillComponent { BigDecimal amount(); String description(); }
