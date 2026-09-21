package com.healthsync.decorator;
import java.math.BigDecimal;
public class HomeVisitServiceDecorator extends AppointmentServiceDecorator {
    public HomeVisitServiceDecorator(AppointmentServiceOption delegate) { super(delegate); }
    public String description() { return delegate.description() + " + Home Visit"; }
    public BigDecimal additionalCharge() { return delegate.additionalCharge().add(new BigDecimal("1000")); }
}
