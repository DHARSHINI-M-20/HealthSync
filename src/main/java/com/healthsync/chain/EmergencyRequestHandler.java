package com.healthsync.chain;
import com.healthsync.model.EmergencyRequest;
/** Handler base for staged emergency intake and escalation. */
public abstract class EmergencyRequestHandler {
    private EmergencyRequestHandler next;
    public EmergencyRequestHandler linkWith(EmergencyRequestHandler next) {
        EmergencyRequestHandler tail = this;
        while (tail.next != null) tail = tail.next;
        tail.next = next;
        return this;
    }
    public final void handle(EmergencyRequest request) { process(request); if (next != null) next.handle(request); }
    protected abstract void process(EmergencyRequest request);
}
