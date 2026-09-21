package com.healthsync.composite;
import com.healthsync.visitor.MedicalRecordVisitor;
/** Leaf clinical narrative component. */
public record ClinicalNote(String text) implements MedicalRecordComponent {
    public String summary() { return text; } public void accept(MedicalRecordVisitor visitor) { visitor.visitNote(this); }
}
