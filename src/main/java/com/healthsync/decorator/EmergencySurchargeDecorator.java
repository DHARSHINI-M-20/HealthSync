package com.healthsync.decorator;
import java.math.BigDecimal;
public class EmergencySurchargeDecorator extends BillDecorator {
    private final BigDecimal surcharge; public EmergencySurchargeDecorator(BillComponent delegate, BigDecimal surcharge) { super(delegate); this.surcharge=surcharge; }
    public BigDecimal amount() { return delegate.amount().add(surcharge); } public String description() { return delegate.description() + " + emergency surcharge"; }
}
