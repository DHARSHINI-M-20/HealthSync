package com.healthsync.command;
import com.healthsync.service.AppointmentService;
/** Independently executable completion action. */
public class CompleteAppointmentCommand implements Command { private final AppointmentService service; private final String appointmentId; public CompleteAppointmentCommand(AppointmentService service, String appointmentId) { this.service=service; this.appointmentId=appointmentId; } public void execute() { service.complete(appointmentId); } }
