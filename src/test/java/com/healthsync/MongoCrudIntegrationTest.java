package com.healthsync;

import com.healthsync.model.Appointment;
import com.healthsync.repository.mongo.MongoAppointmentRepository;
import com.healthsync.state.RequestedState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** Opt-in CRUD smoke test for a locally running MongoDB instance. */
class MongoCrudIntegrationTest {
    private MongoAppointmentRepository repository;
    private String appointmentId;

    @BeforeEach
    void setUp() {
        Assumptions.assumeTrue(Boolean.parseBoolean(System.getProperty("mongo.integration", "false")),
                "Enable with -Dmongo.integration=true");
        repository = new MongoAppointmentRepository();
        appointmentId = "mongo-test-" + UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        if (repository != null && appointmentId != null) repository.deleteById(appointmentId);
    }

    @Test
    void appointmentRepositorySupportsCreateReadUpdateDelete() {
        Appointment created = new Appointment(appointmentId, "P-MONGO", "D-MONGO",
                Instant.parse("2026-09-10T10:00:00Z"), new RequestedState());

        assertSame(created, repository.save(created));
        assertTrue(repository.findById(appointmentId).isPresent());
        assertEquals("P-MONGO", repository.findByPatientId("P-MONGO").get(0).getPatientId());
        assertEquals("D-MONGO", repository.findByDoctorId("D-MONGO").get(0).getDoctorId());

        created.confirm();
        repository.save(created);
        assertEquals("CONFIRMED", repository.findById(appointmentId).orElseThrow().getStatus().name());

        repository.deleteById(appointmentId);
        assertTrue(repository.findById(appointmentId).isEmpty());
    }
}
