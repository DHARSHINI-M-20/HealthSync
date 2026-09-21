package com.healthsync.decorator;
import java.math.BigDecimal;
public class BloodTestServiceDecorator extends AppointmentServiceDecorator {
    public BloodTestServiceDecorator(AppointmentServiceOption delegate) { super(delegate); }
    public String description() { return delegate.description() + " + Blood Test"; }
    public BigDecimal additionalCharge() { return delegate.additionalCharge().add(new BigDecimal("350")); }
}
