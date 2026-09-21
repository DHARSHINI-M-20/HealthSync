package com.healthsync.chain;
import com.healthsync.model.Appointment;
public class AvailabilityCheckHandler extends AppointmentRequestHandler { protected boolean check(Appointment appointment) { /* TODO: Query doctor schedule. */ return true; } }
