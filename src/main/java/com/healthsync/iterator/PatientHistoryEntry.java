package com.healthsync.iterator;
import java.time.Instant;
/** One chronological event from a patient's appointments or clinical records. */
public record PatientHistoryEntry(Instant occurredAt, String type, String referenceId, String summary) { }
