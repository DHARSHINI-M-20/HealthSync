package com.healthsync.memento;
import com.healthsync.model.MedicalRecord;
/** Originator for save/restore of immutable medical-record versions. */
public class MedicalRecordDraft {
    private MedicalRecord currentRecord;
    public MedicalRecordDraft(MedicalRecord currentRecord) { this.currentRecord=currentRecord; }
    public void replace(MedicalRecord record) { currentRecord=record; }
    public MedicalRecord currentRecord() { return currentRecord; }
    public MedicalRecordMemento save() { return new MedicalRecordMemento(currentRecord); }
    public void restore(MedicalRecordMemento memento) { currentRecord=memento.record(); }
}
