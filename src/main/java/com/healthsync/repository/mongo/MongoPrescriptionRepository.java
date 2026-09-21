package com.healthsync.repository.mongo;
import com.healthsync.flyweight.MedicationCatalogItem;
import com.healthsync.model.Prescription;
import com.healthsync.model.PrescriptionItem;
import com.healthsync.repository.PrescriptionRepository;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for prescriptions and embedded medication directions. */
public class MongoPrescriptionRepository extends AbstractMongoRepository<Prescription> implements PrescriptionRepository {
    public MongoPrescriptionRepository() { super("prescriptions", new PrescriptionMapper()); }
    public List<Prescription> findByPatientId(String patientId) { return collection().find(eq("patientId", patientId)).map(mapper()::fromDocument).into(new ArrayList<>()); }
    private static class PrescriptionMapper implements DocumentMapper<Prescription> {
        public Document toDocument(Prescription prescription) {
            List<Document> items = prescription.getItems().stream().map(item -> new Document("code", item.medication().code()).append("name", item.medication().name()).append("strength", item.medication().strength()).append("dosage", item.dosage()).append("instructions", item.instructions())).toList();
            return new Document("_id", prescription.getId()).append("patientId", prescription.getPatientId()).append("doctorId", prescription.getDoctorId()).append("issuedAt", Date.from(prescription.getIssuedAt())).append("items", items);
        }
        public Prescription fromDocument(Document d) {
            List<PrescriptionItem> items = new ArrayList<>();
            for (Document item : d.getList("items", Document.class, List.of())) items.add(new PrescriptionItem(new MedicationCatalogItem(item.getString("code"), item.getString("name"), item.getString("strength")), item.getString("dosage"), item.getString("instructions")));
            return new Prescription(d.getString("_id"), d.getString("patientId"), d.getString("doctorId"), d.getDate("issuedAt").toInstant(), items);
        }
    }
}
