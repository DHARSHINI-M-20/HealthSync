package com.healthsync.mediator;

/** Doctor colleague that receives care-team messages through the mediator. */
public class DoctorColleague extends HealthcareParticipant {
    public DoctorColleague(String name, HealthcareMediator mediator) { super(name, mediator); }
    @Override public void receive(String message, String sender) {
        log("Doctor received from " + sender + ": " + message);
    }
}