package com.healthsync.decorator;
import java.math.BigDecimal;
/** Required appointment consultation service. */
public record BaseAppointmentService(BigDecimal consultationCharge) implements AppointmentServiceOption {
    public String description() { return "Appointment consultation"; }
    public BigDecimal additionalCharge() { return consultationCharge; }
}
