package com.healthsync.repository.mongo;

import com.healthsync.model.Invoice;
import com.healthsync.repository.InvoiceRepository;
import org.bson.Document;
import java.math.BigDecimal;
import static com.mongodb.client.model.Filters.eq;

/** MongoDB implementation for invoice documents. */
public class MongoInvoiceRepository extends AbstractMongoRepository<Invoice> implements InvoiceRepository {
    public MongoInvoiceRepository() { super("invoices", new InvoiceMapper()); }
    private static class InvoiceMapper implements DocumentMapper<Invoice> {
        @Override public Document toDocument(Invoice invoice) {
            return new Document("_id", invoice.getId()).append("patientId", invoice.getPatientId())
                    .append("appointmentId", invoice.getAppointmentId()).append("total", invoice.getTotal().toPlainString());
        }
        @Override public Invoice fromDocument(Document d) {
            return new Invoice(d.getString("_id"), d.getString("patientId"), d.getString("appointmentId"),
                    new BigDecimal(d.getString("total")));
        }
    }
}