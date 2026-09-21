package com.healthsync.state;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
/** Encapsulates permitted appointment lifecycle behavior. */
public interface AppointmentState {
    void confirm(Appointment appointment); void checkIn(Appointment appointment); void startConsultation(Appointment appointment);
    void complete(Appointment appointment); void cancel(Appointment appointment); void reject(Appointment appointment);
    AppointmentStatus status();
}
