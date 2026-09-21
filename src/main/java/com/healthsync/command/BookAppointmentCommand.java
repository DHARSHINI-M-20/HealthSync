package com.healthsync.command;
import com.healthsync.model.Appointment;
import com.healthsync.service.AppointmentService;
import com.healthsync.service.AppointmentBookingRequest;
public class BookAppointmentCommand implements Command {
    private final AppointmentService service; private final AppointmentBookingRequest request; private Appointment result;
    public BookAppointmentCommand(AppointmentService service, AppointmentBookingRequest request) { this.service=service; this.request=request; }
    public void execute() { result=service.book(request); }
    public Appointment getResult() { return result; }
}
