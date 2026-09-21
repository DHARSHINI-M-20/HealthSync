package com.healthsync.iterator;

import com.healthsync.model.Appointment;
import com.healthsync.model.MedicalRecord;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/** Iterates a patient's records without exposing repository details. */
public class PatientHistoryIterator implements Iterator<PatientHistoryEntry> {

    private final Iterator<PatientHistoryEntry> delegate;

    /** Merges and orders persisted record and appointment history without exposing collection internals. */
    public PatientHistoryIterator(List<MedicalRecord> records, List<Appointment> appointments) {
        List<PatientHistoryEntry> entries = new ArrayList<>();
        records.forEach(record -> entries.add(new PatientHistoryEntry(record.getDate(), "MEDICAL_RECORD", record.getRecordId(), record.getDiagnosis())));
        appointments.forEach(appointment -> entries.add(new PatientHistoryEntry(appointment.getScheduledAt(), "APPOINTMENT", appointment.getId(), appointment.getStatus().name())));
        entries.sort(Comparator.comparing(PatientHistoryEntry::occurredAt));
        this.delegate = entries.iterator();
    }

    /** Convenience factory that hides the two repository calls from callers. */
    public static PatientHistoryIterator forPatient(String patientId,
                                                     List<MedicalRecord> records,
                                                     List<Appointment> appointments) {
        return new PatientHistoryIterator(
                Optional.ofNullable(records).orElse(List.of()),
                Optional.ofNullable(appointments).orElse(List.of()));
    }

    @Override public boolean hasNext() { return delegate.hasNext(); }
    @Override public PatientHistoryEntry next() { return delegate.next(); }
}