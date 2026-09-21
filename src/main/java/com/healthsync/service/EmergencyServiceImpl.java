package com.healthsync.service;

import com.healthsync.model.EmergencyCase;
import com.healthsync.repository.EmergencyCaseRepository;
import java.util.UUID;

/** Registers emergency cases and routes them through the responsibility chain. */
public class EmergencyServiceImpl implements EmergencyService {
    private final EmergencyCaseRepository repository;
    public EmergencyServiceImpl(EmergencyCaseRepository repository) { this.repository = repository; }
    @Override public EmergencyCase register(EmergencyCase emergencyCase) {
        if (emergencyCase == null) throw new IllegalArgumentException("Emergency case is required");
        EmergencyCase persisted = new EmergencyCase(
                emergencyCase.getId() == null ? UUID.randomUUID().toString() : emergencyCase.getId(),
                emergencyCase.getPatientId(), emergencyCase.getPriority());
        return repository.save(persisted);
    }
}