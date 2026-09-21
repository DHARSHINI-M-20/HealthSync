package com.healthsync.repository.mongo;
import com.healthsync.model.MedicalRecord;
import com.healthsync.model.RecordType;
import com.healthsync.repository.MedicalRecordRepository;
import org.bson.Document;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for complete persisted clinical records. */
public class MongoMedicalRecordRepository extends AbstractMongoRepository<MedicalRecord> implements MedicalRecordRepository {
    public MongoMedicalRecordRepository() { super("medical_records", new MedicalRecordMapper()); }
    public List<MedicalRecord> findByPatientId(String patientId) { return collection().find(eq("patientId", patientId)).map(mapper()::fromDocument).into(new java.util.ArrayList<>()); }
    private static class MedicalRecordMapper implements DocumentMapper<MedicalRecord> {
        public Document toDocument(MedicalRecord record) { return new Document("_id", record.getRecordId()).append("patientId", record.getPatientId()).append("doctorId", record.getDoctorId()).append("diagnosis", record.getDiagnosis()).append("symptoms", record.getSymptoms()).append("prescription", record.getPrescription()).append("testResults", record.getTestResults()).append("notes", record.getNotes()).append("date", Date.from(record.getDate())).append("recordType", record.getRecordType().name()); }
        public MedicalRecord fromDocument(Document d) { return new MedicalRecord(d.getString("_id"), d.getString("patientId"), d.getString("doctorId"), d.getString("diagnosis"), d.getList("symptoms", String.class, List.of()), d.getString("prescription"), d.getList("testResults", String.class, List.of()), d.getString("notes"), d.getDate("date").toInstant(), RecordType.valueOf(d.getString("recordType"))); }
    }
}
