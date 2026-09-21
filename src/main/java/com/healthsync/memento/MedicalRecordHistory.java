package com.healthsync.memento;

import com.healthsync.model.MedicalRecord;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/** Caretaker retaining previous record snapshots for undo/restore. */
public class MedicalRecordHistory {
    private final Deque<MedicalRecordMemento> versions = new ArrayDeque<>();
    private final Deque<MedicalRecordMemento> redone = new ArrayDeque<>();

    public void save(MedicalRecordMemento memento) {
        versions.push(memento);
        redone.clear();
    }
    public MedicalRecordMemento restorePrevious() {
        if (versions.isEmpty()) throw new IllegalStateException("No earlier medical-record version exists");
        MedicalRecordMemento current = versions.pop();
        redone.push(current);
        return current;
    }
    public MedicalRecordMemento redo() {
        if (redone.isEmpty()) throw new IllegalStateException("No redone medical-record version exists");
        MedicalRecordMemento next = redone.pop();
        versions.push(next);
        return next;
    }
    public boolean hasPrevious() { return !versions.isEmpty(); }
    public boolean hasNext() { return !redone.isEmpty(); }
    public int versionCount() { return versions.size(); }
    public List<MedicalRecordMemento> snapshot() { return List.copyOf(versions); }
}