package com.healthsync.mediator;
/** Central communication contract for care-team participants. */
public interface HealthcareMediator { void register(HealthcareParticipant participant); void send(String message, HealthcareParticipant sender); }
