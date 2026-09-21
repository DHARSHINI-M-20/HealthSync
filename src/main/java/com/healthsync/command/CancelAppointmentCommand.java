package com.healthsync.command;
import com.healthsync.service.AppointmentService;
/** Independently executable cancellation action. */
public class CancelAppointmentCommand implements Command { private final AppointmentService service; private final String appointmentId; public CancelAppointmentCommand(AppointmentService service, String appointmentId) { this.service=service; this.appointmentId=appointmentId; } public void execute() { service.cancel(appointmentId); } }
