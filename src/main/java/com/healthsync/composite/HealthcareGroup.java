package com.healthsync.composite;
import java.util.ArrayList;
import java.util.List;
/** Composite base used by hospitals, departments, and service groups. */
public abstract class HealthcareGroup implements HealthcareNode {
    private final String name; private final List<HealthcareNode> children=new ArrayList<>();
    protected HealthcareGroup(String name) { this.name=name; }
    public String name() { return name; }
    public void add(HealthcareNode child) { children.add(child); }
    public void remove(HealthcareNode child) { children.remove(child); }
    public List<HealthcareNode> children() { return List.copyOf(children); }
}
