package com.healthsync.command;
import com.healthsync.service.AppointmentService;
/** Independently executable confirmation action. */
public class ConfirmAppointmentCommand implements Command { private final AppointmentService service; private final String appointmentId; public ConfirmAppointmentCommand(AppointmentService service, String appointmentId) { this.service=service; this.appointmentId=appointmentId; } public void execute() { service.confirm(appointmentId); } }
