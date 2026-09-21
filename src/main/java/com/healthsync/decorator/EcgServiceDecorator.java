package com.healthsync.decorator;
import java.math.BigDecimal;
public class EcgServiceDecorator extends AppointmentServiceDecorator {
    public EcgServiceDecorator(AppointmentServiceOption delegate) { super(delegate); }
    public String description() { return delegate.description() + " + ECG"; }
    public BigDecimal additionalCharge() { return delegate.additionalCharge().add(new BigDecimal("500")); }
}
