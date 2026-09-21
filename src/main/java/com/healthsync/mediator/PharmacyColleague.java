package com.healthsync.mediator;

/** Pharmacy colleague that receives care-team messages through the mediator. */
public class PharmacyColleague extends HealthcareParticipant {
    public PharmacyColleague(String name, HealthcareMediator mediator) { super(name, mediator); }
    @Override public void receive(String message, String sender) {
        log("Pharmacy received from " + sender + ": " + message);
    }
}