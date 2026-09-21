package com.healthsync.repository.mongo;
import com.healthsync.model.Appointment;
import com.healthsync.repository.AppointmentRepository;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.state.AppointmentStates;
import org.bson.Document;
import java.time.Instant;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for appointment documents. */
public class MongoAppointmentRepository extends AbstractMongoRepository<Appointment> implements AppointmentRepository {
    public MongoAppointmentRepository() { super("appointments", new AppointmentMapper()); }
    public List<Appointment> findByPatientId(String patientId) { return collection().find(eq("patientId", patientId)).map(mapper()::fromDocument).into(new java.util.ArrayList<>()); }
    public List<Appointment> findByDoctorId(String doctorId) { return collection().find(eq("doctorId", doctorId)).map(mapper()::fromDocument).into(new java.util.ArrayList<>()); }
    private static class AppointmentMapper implements DocumentMapper<Appointment> {
        public Document toDocument(Appointment appointment) { return new Document("_id", appointment.getId()).append("patientId", appointment.getPatientId()).append("doctorId", appointment.getDoctorId()).append("scheduledAt", java.util.Date.from(appointment.getScheduledAt())).append("status", appointment.getStatus().name()).append("priorityType", appointment.getPriorityType()).append("priorityScore", appointment.getPriorityScore()); }
        public Appointment fromDocument(Document d) { String priorityType=d.getString("priorityType"); return new Appointment(d.getString("_id"), d.getString("patientId"), d.getString("doctorId"), d.getDate("scheduledAt").toInstant(), AppointmentStates.from(AppointmentStatus.valueOf(d.getString("status"))), priorityType == null ? "NORMAL" : priorityType, d.getInteger("priorityScore", 1)); }
    }
}
