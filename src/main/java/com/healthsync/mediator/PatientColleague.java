package com.healthsync.mediator;

/** Patient colleague that receives care-team messages through the mediator. */
public class PatientColleague extends HealthcareParticipant {
    public PatientColleague(String name, HealthcareMediator mediator) { super(name, mediator); }
    @Override public void receive(String message, String sender) {
        log("Patient received from " + sender + ": " + message);
    }
}