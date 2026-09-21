package com.healthsync.repository.mongo;
import com.healthsync.model.Notification;
import com.healthsync.repository.NotificationRepository;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for notification delivery audit documents. */
public class MongoNotificationRepository extends AbstractMongoRepository<Notification> implements NotificationRepository {
    public MongoNotificationRepository() { super("notifications", new NotificationMapper()); }
    public List<Notification> findByRecipientId(String recipientId) { return collection().find(eq("recipientId", recipientId)).map(mapper()::fromDocument).into(new ArrayList<>()); }
    private static class NotificationMapper implements DocumentMapper<Notification> {
        public Document toDocument(Notification notification) { return new Document("_id", notification.getId()).append("recipientId", notification.getRecipientId()).append("type", notification.getType()).append("message", notification.getMessage()).append("status", notification.getStatus()).append("createdAt", Date.from(notification.getCreatedAt())); }
        public Notification fromDocument(Document d) { return new Notification(d.getString("_id"), d.getString("recipientId"), d.getString("type"), d.getString("message"), d.getString("status"), d.getDate("createdAt").toInstant()); }
    }
}
