package com.healthsync.repository.mongo;

import com.healthsync.model.EmergencyCase;
import com.healthsync.repository.EmergencyCaseRepository;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

/** MongoDB implementation for emergency-case documents. */
public class MongoEmergencyCaseRepository extends AbstractMongoRepository<EmergencyCase> implements EmergencyCaseRepository {
    public MongoEmergencyCaseRepository() { super("emergency_cases", new EmergencyCaseMapper()); }
    private static class EmergencyCaseMapper implements DocumentMapper<EmergencyCase> {
        @Override public Document toDocument(EmergencyCase ec) {
            return new Document("_id", ec.getId()).append("patientId", ec.getPatientId()).append("priority", ec.getPriority());
        }
        @Override public EmergencyCase fromDocument(Document d) {
            return new EmergencyCase(d.getString("_id"), d.getString("patientId"), d.getInteger("priority", 0));
        }
    }
}