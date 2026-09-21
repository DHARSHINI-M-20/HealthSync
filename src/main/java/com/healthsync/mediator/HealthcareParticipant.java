package com.healthsync.mediator;

import java.util.ArrayList;
import java.util.List;

/** Colleague base class that communicates only via HealthcareMediator. */
public abstract class HealthcareParticipant {
    protected final HealthcareMediator mediator;
    private final String name;
    private final List<String> inbox = new ArrayList<>();
    protected HealthcareParticipant(String name, HealthcareMediator mediator) {
        this.name = name;
        this.mediator = mediator;
        mediator.register(this);
    }
    public String name() { return name; }
    public void send(String message) { mediator.send(message, this); }
    public abstract void receive(String message, String sender);
    public List<String> inbox() { return List.copyOf(inbox); }
    protected void log(String message) { inbox.add(message); }
}