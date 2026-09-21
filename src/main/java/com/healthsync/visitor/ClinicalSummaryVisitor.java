package com.healthsync.visitor;
import com.healthsync.composite.ClinicalNote;
import com.healthsync.composite.RecordSection;
/** Visitor skeleton for clinical summary extraction. */
public class ClinicalSummaryVisitor implements MedicalRecordVisitor {
    public void visitSection(RecordSection section) { /* TODO: Append section summary. */ }
    public void visitNote(ClinicalNote note) { /* TODO: Append note summary. */ }
}
