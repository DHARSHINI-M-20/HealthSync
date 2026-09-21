package com.healthsync.decorator;
import java.math.BigDecimal;
public class SpecialistConsultationDecorator extends AppointmentServiceDecorator {
    public SpecialistConsultationDecorator(AppointmentServiceOption delegate) { super(delegate); }
    public String description() { return delegate.description() + " + Specialist Consultation"; }
    public BigDecimal additionalCharge() { return delegate.additionalCharge().add(new BigDecimal("800")); }
}
