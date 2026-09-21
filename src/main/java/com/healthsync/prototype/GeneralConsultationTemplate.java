package com.healthsync.prototype;
import com.healthsync.model.RecordType;
import java.util.List;
/** Prototype defaults for routine general consultations. */
public final class GeneralConsultationTemplate extends MedicalRecordTemplate {
    public GeneralConsultationTemplate() { super(RecordType.GENERAL_CONSULTATION); withNotes("General consultation assessment.").withTestResults(List.of()); }
}
