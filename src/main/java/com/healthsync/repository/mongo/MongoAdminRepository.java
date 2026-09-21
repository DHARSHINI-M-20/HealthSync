package com.healthsync.repository.mongo;
import com.healthsync.model.Admin;
import com.healthsync.repository.AdminRepository;
import org.bson.Document;
import java.util.Optional;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for administrative accounts. */
public class MongoAdminRepository extends AbstractMongoRepository<Admin> implements AdminRepository {
    public MongoAdminRepository() { super("administrators", new AdminMapper()); }
    public Optional<Admin> findByEmail(String email) { return Optional.ofNullable(collection().find(eq("email", email)).first()).map(mapper()::fromDocument); }
    private static class AdminMapper implements DocumentMapper<Admin> {
        public Document toDocument(Admin admin) { return new Document("_id", admin.getId()).append("fullName", admin.getFullName()).append("email", admin.getEmail()).append("passwordHash", admin.getPasswordHash()).append("role", admin.getRole().name()); }
        public Admin fromDocument(Document d) { return new Admin(d.getString("_id"), d.getString("fullName"), d.getString("email"), d.getString("passwordHash")); }
    }
}
