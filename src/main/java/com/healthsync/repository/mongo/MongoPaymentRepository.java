package com.healthsync.repository.mongo;
import com.healthsync.model.Payment;
import com.healthsync.repository.PaymentRepository;
import org.bson.Document;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
/** MongoDB implementation for payment transactions. */
public class MongoPaymentRepository extends AbstractMongoRepository<Payment> implements PaymentRepository {
    public MongoPaymentRepository() { super("payments", new PaymentMapper()); }
    public List<Payment> findByInvoiceId(String invoiceId) { return collection().find(eq("invoiceId", invoiceId)).map(mapper()::fromDocument).into(new ArrayList<>()); }
    private static class PaymentMapper implements DocumentMapper<Payment> {
        public Document toDocument(Payment payment) { return new Document("_id", payment.getId()).append("invoiceId", payment.getInvoiceId()).append("amount", payment.getAmount().toPlainString()).append("method", payment.getMethod()).append("status", payment.getStatus()).append("paidAt", Date.from(payment.getPaidAt())); }
        public Payment fromDocument(Document d) { return new Payment(d.getString("_id"), d.getString("invoiceId"), new BigDecimal(d.getString("amount")), d.getString("method"), d.getString("status"), d.getDate("paidAt").toInstant()); }
    }
}
