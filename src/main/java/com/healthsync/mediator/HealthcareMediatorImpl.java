package com.healthsync.mediator;

import java.util.ArrayList;
import java.util.List;

/** Routes care-team messages without participants depending on one another. */
public class HealthcareMediatorImpl implements HealthcareMediator {
    private final List<HealthcareParticipant> participants = new ArrayList<>();
    @Override public void register(HealthcareParticipant participant) { participants.add(participant); }
    @Override public void send(String message, HealthcareParticipant sender) {
        participants.stream().filter(participant -> participant != sender).forEach(participant -> participant.receive(message, sender.name()));
    }
}