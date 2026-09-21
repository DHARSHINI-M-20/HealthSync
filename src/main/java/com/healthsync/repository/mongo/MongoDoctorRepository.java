package com.healthsync.repository.mongo;
import com.healthsync.model.Doctor;
import com.healthsync.repository.DoctorRepository;
import org.bson.Document;
import java.util.List;
import java.util.Optional;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for the doctors collection. */
public class MongoDoctorRepository extends AbstractMongoRepository<Doctor> implements DoctorRepository {
    public MongoDoctorRepository() { super("doctors", new DoctorMapper()); }
    public Optional<Doctor> findByEmail(String email) { return Optional.ofNullable(collection().find(eq("email", email)).first()).map(mapper()::fromDocument); }
    public List<Doctor> findBySpecialtyCode(String specialtyCode) { return collection().find(eq("specialtyCode", specialtyCode)).map(mapper()::fromDocument).into(new java.util.ArrayList<>()); }
    private static class DoctorMapper implements DocumentMapper<Doctor> {
        public Document toDocument(Doctor doctor) { return new Document("_id", doctor.getId()).append("fullName", doctor.getFullName()).append("email", doctor.getEmail()).append("passwordHash", doctor.getPasswordHash()).append("role", doctor.getRole().name()).append("licenseNumber", doctor.getLicenseNumber()).append("specialtyCode", doctor.getSpecialtyCode()); }
        public Doctor fromDocument(Document d) { return new Doctor(d.getString("_id"), d.getString("fullName"), d.getString("email"), d.getString("passwordHash"), d.getString("licenseNumber"), d.getString("specialtyCode")); }
    }
}
