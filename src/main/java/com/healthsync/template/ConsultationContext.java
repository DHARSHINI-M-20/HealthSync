package com.healthsync.template;
import java.util.ArrayList;
import java.util.List;
/** Shared consultation data and an auditable workflow trail. */
public class ConsultationContext {
    private final String patientId; private final List<String> steps=new ArrayList<>();
    public ConsultationContext(String patientId) { this.patientId=patientId; }
    public String patientId() { return patientId; }
    public void addStep(String step) { steps.add(step); }
    public List<String> steps() { return List.copyOf(steps); }
}
