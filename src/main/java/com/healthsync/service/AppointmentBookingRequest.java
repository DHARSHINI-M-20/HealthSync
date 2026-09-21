package com.healthsync.service;
import com.healthsync.strategy.AppointmentPriorityStrategy;
import java.time.Instant;
/** Input for booking; the selected strategy supplies its priority metadata. */
public record AppointmentBookingRequest(String patientId, String doctorId, Instant scheduledAt, AppointmentPriorityStrategy priorityStrategy) { }
