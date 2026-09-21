package com.healthsync.repository.mongo;
import com.healthsync.model.Nurse;
import com.healthsync.repository.NurseRepository;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for nursing staff accounts. */
public class MongoNurseRepository extends AbstractMongoRepository<Nurse> implements NurseRepository {
    public MongoNurseRepository() { super("nurses", new NurseMapper()); }
    public Optional<Nurse> findByEmail(String email) { return Optional.ofNullable(collection().find(eq("email", email)).first()).map(mapper()::fromDocument); }
    public List<Nurse> findByDepartment(String department) { return collection().find(eq("department", department)).map(mapper()::fromDocument).into(new ArrayList<>()); }
    private static class NurseMapper implements DocumentMapper<Nurse> {
        public Document toDocument(Nurse nurse) { return new Document("_id", nurse.getId()).append("fullName", nurse.getFullName()).append("email", nurse.getEmail()).append("passwordHash", nurse.getPasswordHash()).append("role", nurse.getRole().name()).append("nurseNumber", nurse.getNurseNumber()).append("department", nurse.getDepartment()); }
        public Nurse fromDocument(Document d) { return new Nurse(d.getString("_id"), d.getString("fullName"), d.getString("email"), d.getString("passwordHash"), d.getString("nurseNumber"), d.getString("department")); }
    }
}
