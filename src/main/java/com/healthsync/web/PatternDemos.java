package com.healthsync.web;

import com.healthsync.model.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Invokes the project's existing GoF implementations with isolated sample values. */
final class PatternDemos {
    private PatternDemos() { }

    static String run(String pattern, String input) {
        String value = input == null || input.isBlank() ? "sample" : input.trim();
        return switch (pattern) {
            case "Singleton" -> "SessionContext.getInstance() returns the shared session context: " + (com.healthsync.util.SessionContext.getInstance() == com.healthsync.util.SessionContext.getInstance());
            case "Factory Method" -> { var user = new com.healthsync.factory.PatientFactory().create(new com.healthsync.factory.UserCreationData("demo-1", value, "demo@example.com", "hash", Role.PATIENT, "P-100", null, null, null, null)); yield "PatientFactory created " + user.getClass().getSimpleName() + " for " + user.getFullName(); }
            case "Abstract Factory" -> { var factory = new com.healthsync.abstractfactory.ClinicFactory(); yield "ClinicFactory created the " + factory.createAppointmentService().environmentName() + " appointment, prescription, and notification family."; }
            case "Builder" -> { var record = new com.healthsync.builder.MedicalRecordBuilder().withRecordId("demo-record").withPatientId("demo-patient").withDoctorId("demo-doctor").withDiagnosis(value).withRecordType(RecordType.GENERAL_CONSULTATION).build(); yield "Built MedicalRecord " + record.getRecordId() + " with diagnosis: " + record.getDiagnosis(); }
            case "Prototype" -> { var prototype = new com.healthsync.prototype.GeneralConsultationTemplate(); var copy = prototype.copy(); yield "Cloned " + copy.getClass().getSimpleName() + "; clone is independent: " + (copy != prototype); }
            case "Adapter" -> { var adapter = new com.healthsync.adapter.PaymentGatewayAdapter((ref, amount) -> true); var result = adapter.process(new com.healthsync.adapter.PaymentRequest("invoice-demo", new BigDecimal("25.00"), value)); yield "PaymentGatewayAdapter: " + result.message() + " (" + result.transactionReference() + ")"; }
            case "Bridge" -> { new com.healthsync.bridge.ConsultationService((patient, service) -> {}).provideTo(value); yield "ConsultationService used its DeliveryMethod abstraction for patient " + value + "."; }
            case "Composite" -> { var dept = new com.healthsync.composite.Department("Care"); dept.add(new com.healthsync.composite.HealthcareServiceNode(value)); yield "Department composite " + dept.name() + " contains " + dept.children().size() + " leaf: " + dept.children().get(0).name(); }
            case "Decorator" -> { com.healthsync.decorator.AppointmentServiceOption option = new com.healthsync.decorator.SpecialistConsultationDecorator(new com.healthsync.decorator.BaseAppointmentService(new BigDecimal("1000"))); yield option.description() + " — total add-on charge " + option.additionalCharge(); }
            case "Facade" -> "HealthcareFacade is the existing unified workflow over appointment, record, payment, and notification services; invoking persistence workflows requires configured services and MongoDB.";
            case "Flyweight" -> { var f = new com.healthsync.flyweight.MedicalInformationFactory(); var a = f.medicineType("DEMO", value, "sample"); var b = f.medicineType("DEMO", "ignored duplicate", "sample"); yield "MedicalInformationFactory reused shared MedicineType: " + (a == b) + " (" + a.name() + ")."; }
            case "Proxy" -> { com.healthsync.util.SessionContext.getInstance().clear(); try { new com.healthsync.proxy.MedicalRecordProxy(id -> java.util.Optional.empty()).findById(value); yield "Unexpectedly authorized."; } catch (SecurityException e) { yield "MedicalRecordProxy denied unauthenticated access: " + e.getMessage(); } }
            case "Chain of Responsibility" -> { var request = new EmergencyRequest("demo-patient", value, 5); new com.healthsync.chain.NurseEmergencyHandler().linkWith(new com.healthsync.chain.GeneralDoctorEmergencyHandler()).handle(request); yield "Emergency chain handling trail: " + request.getHandlingTrail(); }
            case "Command" -> "Command is the existing executable action contract (execute/undo); appointment commands need configured appointment services and persisted data.";
            case "Interpreter" -> { String patientId = value.toUpperCase(java.util.Locale.ROOT); var appointment = new Appointment("demo-appointment", patientId, "demo-doctor", Instant.now(), new com.healthsync.state.RequestedState()); boolean matched = new com.healthsync.interpreter.ReportQueryInterpreter().parse("PATIENT = " + value).interpret(appointment); yield "ReportQueryInterpreter evaluated PATIENT = " + value + " against a sample appointment: " + matched; }
            case "Iterator" -> { var iterator = new com.healthsync.iterator.PatientHistoryIterator(List.of(), List.of()); yield "PatientHistoryIterator traversed the sample history; entries available: " + iterator.hasNext(); }
            case "Mediator" -> { var mediator = new com.healthsync.mediator.HealthcareMediatorImpl(); var doctor = new com.healthsync.mediator.DoctorColleague("Doctor", mediator); var pharmacy = new com.healthsync.mediator.PharmacyColleague("Pharmacy", mediator); doctor.send(value); yield "HealthcareMediatorImpl routed the message; pharmacy inbox: " + pharmacy.inbox(); }
            case "Memento" -> { var record = new com.healthsync.builder.MedicalRecordBuilder().withRecordId("demo-record").withPatientId("demo-patient").withDoctorId("demo-doctor").withDiagnosis(value).withRecordType(RecordType.GENERAL_CONSULTATION).build(); var draft = new com.healthsync.memento.MedicalRecordDraft(record); var saved = draft.save(); draft.replace(new com.healthsync.builder.MedicalRecordBuilder().withRecordId("demo-record").withPatientId("demo-patient").withDoctorId("demo-doctor").withDiagnosis("temporary edit").withRecordType(RecordType.GENERAL_CONSULTATION).build()); draft.restore(saved); yield "MedicalRecordDraft restored saved diagnosis: " + draft.currentRecord().getDiagnosis(); }
            case "Observer" -> { var publisher = new com.healthsync.observer.DomainEventPublisher(); var received = new java.util.concurrent.atomic.AtomicReference<String>(); publisher.subscribe(event -> received.set(event.type())); publisher.publish(new com.healthsync.observer.DomainEvent(value, "demo", Instant.now())); yield "DomainEventPublisher delivered event to subscriber: " + received.get(); }
            case "State" -> { var appointment = new Appointment("demo-appointment", "demo-patient", "demo-doctor", Instant.now(), new com.healthsync.state.RequestedState()); appointment.confirm(); yield "Appointment State transition: REQUESTED → " + appointment.getStatus(); }
            case "Strategy" -> { var strategy = new com.healthsync.strategy.NormalPriority(); yield "NormalPriority strategy: type=" + strategy.type() + ", score=" + strategy.priorityScore(); }
            case "Template Method" -> "ReportGenerator defines the template method generate(criteria); this project's appointment report data extraction is currently a TODO.";
            case "Visitor" -> { var note = new com.healthsync.composite.ClinicalNote(value); note.accept(new com.healthsync.visitor.ClinicalSummaryVisitor()); yield "ClinicalNote.accept(ClinicalSummaryVisitor) dispatched through the existing Visitor API; summary extraction is currently a TODO."; }
            default -> "Unknown pattern. Choose one of the listed demonstrations.";
        };
    }
}
