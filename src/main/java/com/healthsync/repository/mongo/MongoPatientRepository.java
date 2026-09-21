package com.healthsync.repository.mongo;
import com.healthsync.model.Patient;
import com.healthsync.repository.PatientRepository;
import org.bson.Document;
import java.util.Optional;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for the patients collection. */
public class MongoPatientRepository extends AbstractMongoRepository<Patient> implements PatientRepository {
    public MongoPatientRepository() { super("patients", new PatientMapper()); }
    public Optional<Patient> findByEmail(String email) { return Optional.ofNullable(collection().find(eq("email", email)).first()).map(mapper()::fromDocument); }
    private static class PatientMapper implements DocumentMapper<Patient> {
        public Document toDocument(Patient patient) { return new Document("_id", patient.getId()).append("fullName", patient.getFullName()).append("email", patient.getEmail()).append("passwordHash", patient.getPasswordHash()).append("role", patient.getRole().name()).append("patientNumber", patient.getPatientNumber()); }
        public Patient fromDocument(Document d) { return new Patient(d.getString("_id"), d.getString("fullName"), d.getString("email"), d.getString("passwordHash"), d.getString("patientNumber")); }
    }
}
