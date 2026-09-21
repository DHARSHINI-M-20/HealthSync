package com.healthsync.mediator;

/** Nurse colleague that receives care-team messages through the mediator. */
public class NurseColleague extends HealthcareParticipant {
    public NurseColleague(String name, HealthcareMediator mediator) { super(name, mediator); }
    @Override public void receive(String message, String sender) {
        log("Nurse received from " + sender + ": " + message);
    }
}