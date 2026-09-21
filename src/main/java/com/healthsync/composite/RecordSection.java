package com.healthsync.composite;
import com.healthsync.visitor.MedicalRecordVisitor;
import java.util.ArrayList;
import java.util.List;
/** Composite section which can contain observations and nested sections. */
public class RecordSection implements MedicalRecordComponent {
    private final String title; private final List<MedicalRecordComponent> children = new ArrayList<>();
    public RecordSection(String title) { this.title = title; }
    public void add(MedicalRecordComponent component) { children.add(component); }
    public String summary() { return title; }
    public void accept(MedicalRecordVisitor visitor) { visitor.visitSection(this); children.forEach(c -> c.accept(visitor)); }
    // TODO: Expose immutable children for presentation/reporting.
}
