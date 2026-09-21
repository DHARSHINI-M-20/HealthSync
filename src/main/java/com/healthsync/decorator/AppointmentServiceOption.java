package com.healthsync.decorator;
import java.math.BigDecimal;
/** Appointment service package that can be enriched with optional clinical services. */
public interface AppointmentServiceOption { String description(); BigDecimal additionalCharge(); }
