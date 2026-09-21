package com.healthsync.model;

import com.healthsync.state.AppointmentState;
import com.healthsync.observer.AppointmentStatusObserver;
import java.time.Instant;

/** Scheduled clinical encounter. */
public class Appointment {
    private final String id;
    private final String patientId;
    private final String doctorId;
    private final Instant scheduledAt;
    private AppointmentState state;
    private final String priorityType;
    private final int priorityScore;
    private final java.util.List<AppointmentStatusObserver> observers = new java.util.ArrayList<>();
    public Appointment(String id, String patientId, String doctorId, Instant scheduledAt, AppointmentState state) {
        this(id, patientId, doctorId, scheduledAt, state, "NORMAL", 1);
    }
    public Appointment(String id, String patientId, String doctorId, Instant scheduledAt, AppointmentState state, String priorityType, int priorityScore) { this.id=id; this.patientId=patientId; this.doctorId=doctorId; this.scheduledAt=scheduledAt; this.state=state; this.priorityType=priorityType; this.priorityScore=priorityScore; }
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public Instant getScheduledAt() { return scheduledAt; }
    public AppointmentState getState() { return state; }
    public AppointmentStatus getStatus() { return state.status(); }
    public String getPriorityType() { return priorityType; }
    public int getPriorityScore() { return priorityScore; }
    public void addObserver(AppointmentStatusObserver observer) { observers.add(observer); }
    public void transitionTo(AppointmentState nextState) { AppointmentStatus previous=state.status(); state=nextState; observers.forEach(observer -> observer.onStatusChanged(this, previous)); }
    public void confirm() { state.confirm(this); }
    public void checkIn() { state.checkIn(this); }
    public void startConsultation() { state.startConsultation(this); }
    public void complete() { state.complete(this); }
    public void cancel() { state.cancel(this); }
    public void reject() { state.reject(this); }
}
