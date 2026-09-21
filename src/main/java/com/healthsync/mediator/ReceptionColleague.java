package com.healthsync.mediator;

/** Reception colleague that receives care-team messages through the mediator. */
public class ReceptionColleague extends HealthcareParticipant {
    public ReceptionColleague(String name, HealthcareMediator mediator) { super(name, mediator); }
    @Override public void receive(String message, String sender) {
        log("Reception received from " + sender + ": " + message);
    }
}