package com.healthsync;

import com.healthsync.abstractfactory.HospitalFactory;
import com.healthsync.adapter.PaymentGatewayAdapter;
import com.healthsync.adapter.PaymentRequest;
import com.healthsync.adapter.PaymentResult;
import com.healthsync.chain.EmergencyDepartmentHandler;
import com.healthsync.chain.EmergencyRequestHandler;
import com.healthsync.chain.GeneralDoctorEmergencyHandler;
import com.healthsync.chain.NurseEmergencyHandler;
import com.healthsync.chain.ReceptionEmergencyHandler;
import com.healthsync.chain.SpecialistEmergencyHandler;
import com.healthsync.command.BookAppointmentCommand;
import com.healthsync.decorator.BaseConsultationBill;
import com.healthsync.decorator.SpecialistConsultationDecorator;
import com.healthsync.iterator.PatientHistoryEntry;
import com.healthsync.iterator.PatientHistoryIterator;
import com.healthsync.interpreter.ReportQueryInterpreter;
import com.healthsync.memento.MedicalRecordHistory;
import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.model.EmergencyRequest;
import com.healthsync.model.Invoice;
import com.healthsync.model.MedicalRecord;
import com.healthsync.model.Patient;
import com.healthsync.model.RecordType;
import com.healthsync.model.User;
import com.healthsync.observer.AppointmentStatusObserver;
import com.healthsync.prototype.GeneralConsultationTemplate;
import com.healthsync.proxy.MedicalRecordAccess;
import com.healthsync.proxy.MedicalRecordProxy;
import com.healthsync.repository.AppointmentRepository;
import com.healthsync.service.AppointmentBookingRequest;
import com.healthsync.service.AppointmentManagementService;
import com.healthsync.service.AppointmentService;
import com.healthsync.service.BillingService;
import com.healthsync.service.BillingServiceImpl;
import com.healthsync.service.NotificationService;
import com.healthsync.service.UserManagementService;
import com.healthsync.state.RequestedState;
import com.healthsync.strategy.EmergencyPriority;
import com.healthsync.strategy.NormalPriority;
import com.healthsync.template.ConsultationContext;
import com.healthsync.template.GeneralConsultationProcess;
import com.healthsync.util.SessionContext;
import com.healthsync.visitor.BillingVisitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HealthSyncCoreTest {

    @AfterEach
    void clearSession() {
        SessionContext.getInstance().clear();
    }

    @Test
    void abstractFactoryCreatesOneCompatibleProductFamily() {
        HospitalFactory factory = new HospitalFactory();
        assertNotNull(factory.createAppointmentService());
        assertNotNull(factory.createPrescriptionService());
        assertNotNull(factory.createNotificationService());
    }

    @Test
    void adapterReturnsGatewayDecision() {
        PaymentGatewayAdapter adapter = new PaymentGatewayAdapter((reference, amount) -> amount.signum() > 0);
        PaymentResult result = adapter.process(new PaymentRequest("INV-1", new BigDecimal("50.00"), "REF-1"));
        assertTrue(result.successful());
        assertEquals("REF-1", result.transactionReference());
        assertEquals("Payment approved", result.message());
    }

    @Test
    void builderCreatesImmutableMedicalRecordAndValidatesRequiredFields() {
        MedicalRecord record = new com.healthsync.builder.MedicalRecordBuilder()
                .withRecordId("R-1").withPatientId("P-1").withDoctorId("D-1")
                .withDiagnosis("Flu").addSymptom("Fever")
                .withDate(Instant.parse("2026-01-01T00:00:00Z"))
                .withRecordType(RecordType.GENERAL_CONSULTATION).build();
        assertEquals("Flu", record.getDiagnosis());
        assertEquals(List.of("Fever"), record.getSymptoms());
        assertThrows(IllegalStateException.class, () -> new com.healthsync.builder.MedicalRecordBuilder().build());
    }

    @Test
    void emergencyChainRecordsEveryApplicableStep() {
        EmergencyRequestHandler chain = new ReceptionEmergencyHandler()
                .linkWith(new NurseEmergencyHandler())
                .linkWith(new GeneralDoctorEmergencyHandler())
                .linkWith(new SpecialistEmergencyHandler())
                .linkWith(new EmergencyDepartmentHandler());
        EmergencyRequest request = new EmergencyRequest("P-1", "Chest pain", 10);
        chain.handle(request);
        assertEquals(5, request.getHandlingTrail().size());
        assertThrows(IllegalArgumentException.class, () -> chain.handle(new EmergencyRequest("", "", 1)));
    }

    @Test
    void commandDelegatesBookingAndExposesResult() {
        Appointment expected = appointment("A-1", new RequestedState());
        AppointmentService service = new AppointmentService() {
            public Appointment book(AppointmentBookingRequest request) { return expected; }
            public Appointment cancel(String id) { return expected; }
            public Appointment confirm(String id) { return expected; }
            public Appointment complete(String id) { return expected; }
            public Appointment checkIn(String id) { return expected; }
            public Appointment startConsultation(String id) { return expected; }
            public Appointment reject(String id) { return expected; }
            public List<Appointment> findAll() { return List.of(expected); }
        };
        BookAppointmentCommand command = new BookAppointmentCommand(service,
                new AppointmentBookingRequest("P-1", "D-1", Instant.now(), new NormalPriority()));
        command.execute();
        assertSame(expected, command.getResult());
    }

    @Test
    void decoratorAddsSpecialistCharge() {
        SpecialistConsultationDecorator decorated = new SpecialistConsultationDecorator(
            new com.healthsync.decorator.AppointmentServiceOption() {
                public String description() { return "Consultation"; }
                public BigDecimal additionalCharge() { return new BigDecimal("50.00"); }
            });
        assertEquals(new BigDecimal("850.00"), decorated.additionalCharge());
        assertTrue(decorated.description().contains("Specialist"));
    }

    @Test
    void flyweightReusesMedicationByCode() {
        com.healthsync.flyweight.MedicationCatalog catalog = new com.healthsync.flyweight.MedicationCatalog();
        assertSame(catalog.get("PARA", "Paracetamol", "500mg"), catalog.get("PARA", "Other label", "250mg"));
    }

    @Test
    void interpreterCombinesAppointmentPredicates() {
        Appointment matching = new Appointment("A-1", "P-1", "D-1", Instant.now(), new com.healthsync.state.ConfirmedState(), "EMERGENCY", 100);
        Appointment notMatching = new Appointment("A-2", "P-1", "D-2", Instant.now(), new com.healthsync.state.ConfirmedState(), "NORMAL", 1);
        var expression = new ReportQueryInterpreter().parse("DOCTOR = D-1 AND STATUS = CONFIRMED AND PRIORITY > 50");
        assertTrue(expression.interpret(matching));
        assertFalse(expression.interpret(notMatching));
    }

    @Test
    void iteratorMergesHistoryInChronologicalOrder() {
        Instant first = Instant.parse("2026-01-01T00:00:00Z");
        Instant second = Instant.parse("2026-01-02T00:00:00Z");
        MedicalRecord record = new com.healthsync.builder.MedicalRecordBuilder().withRecordId("R-1").withPatientId("P-1").withDoctorId("D-1")
                .withDate(second).withRecordType(RecordType.GENERAL_CONSULTATION).build();
        PatientHistoryIterator iterator = new PatientHistoryIterator(List.of(record), List.of(appointment("A-1", new RequestedState(), first)));
        PatientHistoryEntry firstEntry = iterator.next();
        assertEquals("APPOINTMENT", firstEntry.type());
        assertEquals("MEDICAL_RECORD", iterator.next().type());
    }

    @Test
    void mementoSupportsRestoreAndRedo() {
        MedicalRecordHistory history = new MedicalRecordHistory();
        MedicalRecord firstRecord = new com.healthsync.builder.MedicalRecordBuilder().withRecordId("R-1").withPatientId("P-1").withDoctorId("D-1").withRecordType(RecordType.GENERAL_CONSULTATION).build();
        MedicalRecord secondRecord = new com.healthsync.builder.MedicalRecordBuilder().withRecordId("R-2").withPatientId("P-1").withDoctorId("D-1").withRecordType(RecordType.GENERAL_CONSULTATION).build();
        var first = new com.healthsync.memento.MedicalRecordMemento(firstRecord);
        var second = new com.healthsync.memento.MedicalRecordMemento(secondRecord);
        history.save(first);
        history.save(second);
        assertSame(second, history.restorePrevious());
        assertSame(second, history.redo());
        assertFalse(history.hasNext());
    }

    @Test
    void proxyAllowsOwnerAndRejectsUnrelatedPatient() {
        MedicalRecord record = new com.healthsync.builder.MedicalRecordBuilder().withRecordId("R-1").withPatientId("P-1").withDoctorId("D-1")
                .withRecordType(RecordType.GENERAL_CONSULTATION).build();
        MedicalRecordAccess target = id -> Optional.of(record);
        MedicalRecordProxy proxy = new MedicalRecordProxy(target);
        SessionContext.getInstance().setCurrentUser(new Patient("P-1", "Patient", "p@example.com", "hash", "PN-1"));
        assertTrue(proxy.findById("R-1").isPresent());
        SessionContext.getInstance().setCurrentUser(new Patient("P-2", "Other", "o@example.com", "hash", "PN-2"));
        assertThrows(SecurityException.class, () -> proxy.findById("R-1"));
    }

    @Test
    void stateAllowsValidAppointmentLifecycleAndRejectsInvalidAction() {
        Appointment appointment = appointment("A-1", new RequestedState());
        appointment.confirm();
        appointment.checkIn();
        appointment.startConsultation();
        appointment.complete();
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());
        assertThrows(IllegalStateException.class, appointment::cancel);
    }

    @Test
    void strategyCarriesEmergencyPriorityMetadata() {
        assertEquals("EMERGENCY", new EmergencyPriority().type());
        assertTrue(new EmergencyPriority().priorityScore() > new NormalPriority().priorityScore());
    }

    @Test
    void templateRunsStableConsultationWorkflow() {
        ConsultationContext context = new ConsultationContext("P-1");
        new GeneralConsultationProcess().conduct(context);
        assertEquals(List.of("Patient registered", "Medical history checked", "General physical examination", "General diagnosis recorded", "Standard prescription issued", "Consultation completed"), context.steps());
    }

    @Test
    void prototypeCopiesTemplateDefaults() {
        GeneralConsultationTemplate original = new GeneralConsultationTemplate();
        var copy = original.copy();
        MedicalRecord record = copy.applyTo(new com.healthsync.builder.MedicalRecordBuilder().withRecordId("R-1").withPatientId("P-1").withDoctorId("D-1")).build();
        assertEquals(RecordType.GENERAL_CONSULTATION, record.getRecordType());
        assertNotSame(original, copy);
    }

    @Test
    void visitorCalculatesConsultationAndMedicationTotals() {
        Patient patient = new Patient("P-1", "Patient", "p@example.com", "hash", "P-1");
        BillingVisitor visitor = new BillingVisitor();
        patient.accept(visitor);
        assertEquals(new BigDecimal("50.00"), visitor.getTotal());
        assertEquals("P-1", visitor.getPatientId());
    }

    @Test
    void appointmentServiceBooksAndPersistsPriority() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
        AppointmentManagementService service = new AppointmentManagementService(repository, new NoopNotifications());
        Appointment appointment = service.book(new AppointmentBookingRequest("P-1", "D-1", Instant.now(), new EmergencyPriority()));
        assertEquals("EMERGENCY", appointment.getPriorityType());
        assertEquals(AppointmentStatus.REQUESTED, appointment.getStatus());
        assertTrue(repository.findById(appointment.getId()).isPresent());
    }

    @Test
    void billingRejectsUnknownInvoiceAndRecordsPayment() {
        InMemoryPaymentRepository payments = new InMemoryPaymentRepository();
        InMemoryInvoiceRepository invoices = new InMemoryInvoiceRepository();
        BillingService billing = new BillingServiceImpl(payments, new InMemoryAppointmentRepository(), invoices);
        Invoice invoice = new Invoice("I-1", "P-1", "A-1", new BigDecimal("50.00"));
        invoices.save(invoice);
        assertEquals("COMPLETED", billing.recordPayment("I-1", new BigDecimal("50.00"), "CARD").getStatus());
        assertThrows(IllegalArgumentException.class, () -> billing.recordPayment("missing", BigDecimal.ONE, "CARD"));
    }

    private static Appointment appointment(String id, com.healthsync.state.AppointmentState state) {
        return appointment(id, state, Instant.now());
    }

    private static Appointment appointment(String id, com.healthsync.state.AppointmentState state, Instant scheduledAt) {
        return new Appointment(id, "P-1", "D-1", scheduledAt, state);
    }

    private static class InMemoryAppointmentRepository implements AppointmentRepository {
        private final Map<String, Appointment> values = new HashMap<>();
        public Appointment save(Appointment entity) { values.put(entity.getId(), entity); return entity; }
        public Optional<Appointment> findById(String id) { return Optional.ofNullable(values.get(id)); }
        public List<Appointment> findAll() { return new ArrayList<>(values.values()); }
        public void deleteById(String id) { values.remove(id); }
        public List<Appointment> findByPatientId(String id) { return values.values().stream().filter(value -> value.getPatientId().equals(id)).toList(); }
        public List<Appointment> findByDoctorId(String id) { return values.values().stream().filter(value -> value.getDoctorId().equals(id)).toList(); }
    }

    private static class InMemoryInvoiceRepository implements com.healthsync.repository.InvoiceRepository {
        private final Map<String, Invoice> values = new HashMap<>();
        public Invoice save(Invoice entity) { values.put(entity.getId(), entity); return entity; }
        public Optional<Invoice> findById(String id) { return Optional.ofNullable(values.get(id)); }
        public List<Invoice> findAll() { return new ArrayList<>(values.values()); }
        public void deleteById(String id) { values.remove(id); }
    }

    private static class InMemoryPaymentRepository implements com.healthsync.repository.PaymentRepository {
        private final Map<String, com.healthsync.model.Payment> values = new HashMap<>();
        public com.healthsync.model.Payment save(com.healthsync.model.Payment entity) { values.put(entity.getId(), entity); return entity; }
        public Optional<com.healthsync.model.Payment> findById(String id) { return Optional.ofNullable(values.get(id)); }
        public List<com.healthsync.model.Payment> findAll() { return new ArrayList<>(values.values()); }
        public void deleteById(String id) { values.remove(id); }
        public List<com.healthsync.model.Payment> findByInvoiceId(String id) { return values.values().stream().filter(value -> value.getInvoiceId().equals(id)).toList(); }
    }

    private static class NoopNotifications implements NotificationService {
        public void send(com.healthsync.bridge.Notification notification) { }
        public void notifyRecipient(String recipientId, String message) { }
        public void notifyAppointmentStatus(Appointment appointment, AppointmentStatus previousStatus) { }
        public List<com.healthsync.model.Notification> findAll() { return List.of(); }
    }
}
