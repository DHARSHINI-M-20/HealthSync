package com.healthsync.ui;

import com.healthsync.abstractfactory.ClinicFactory;
import com.healthsync.abstractfactory.HealthcareFactory;
import com.healthsync.abstractfactory.HospitalFactory;
import com.healthsync.adapter.ExternalPaymentGateway;
import com.healthsync.adapter.Payment;
import com.healthsync.adapter.PaymentGatewayAdapter;
import com.healthsync.controller.AppointmentController;
import com.healthsync.controller.AuthenticationController;
import com.healthsync.controller.MedicalRecordController;
import com.healthsync.controller.PatientController;
import com.healthsync.database.DatabaseConnection;
import com.healthsync.factory.AdminFactory;
import com.healthsync.factory.DoctorFactory;
import com.healthsync.factory.NurseFactory;
import com.healthsync.factory.PatientFactory;
import com.healthsync.factory.UserFactory;
import com.healthsync.repository.mongo.MongoAdminRepository;
import com.healthsync.repository.mongo.MongoAppointmentRepository;
import com.healthsync.repository.mongo.MongoDoctorRepository;
import com.healthsync.repository.mongo.MongoEmergencyCaseRepository;
import com.healthsync.repository.mongo.MongoInvoiceRepository;
import com.healthsync.repository.mongo.MongoMedicalRecordRepository;
import com.healthsync.repository.mongo.MongoNurseRepository;
import com.healthsync.repository.mongo.MongoPatientRepository;
import com.healthsync.repository.mongo.MongoPaymentRepository;
import com.healthsync.repository.mongo.MongoPrescriptionRepository;
import com.healthsync.service.AppointmentManagementService;
import com.healthsync.service.AppointmentService;
import com.healthsync.service.AuthenticationService;
import com.healthsync.service.AuthenticationServiceImpl;
import com.healthsync.service.BillingService;
import com.healthsync.service.BillingServiceImpl;
import com.healthsync.service.CareEnvironmentService;
import com.healthsync.service.EmergencyService;
import com.healthsync.service.EmergencyServiceImpl;
import com.healthsync.service.MedicalRecordService;
import com.healthsync.service.MedicalRecordServiceImpl;
import com.healthsync.service.NotificationService;
import com.healthsync.service.NotificationServiceImpl;
import com.healthsync.service.PrescriptionService;
import com.healthsync.service.PrescriptionServiceImpl;
import com.healthsync.service.UserManagementService;
import com.healthsync.service.UserManagementServiceImpl;
import com.healthsync.util.SessionContext;

import java.util.List;

/** Wires repositories, services, controllers, and shared UI collaborators together once. */
public final class AppContext {

    public final DatabaseConnection database;
    public final SessionContext session;
    public final UserManagementService users;
    public final AppointmentService appointments;
    public final MedicalRecordService records;
    public final PrescriptionService prescriptions;
    public final NotificationService notifications;
    public final EmergencyService emergency;
    public final BillingService billing;
    public final Payment payments;
    public final CareEnvironmentService careEnvironment;
    public final AuthenticationController authController;
    public final PatientController patientController;
    public final AppointmentController appointmentController;
    public final MedicalRecordController recordController;
    public ApplicationFrame appFrame;

    public AppContext() {
        database = DatabaseConnection.getInstance();
        session = SessionContext.getInstance();

        MongoPatientRepository patients = new MongoPatientRepository();
        MongoDoctorRepository doctors = new MongoDoctorRepository();
        MongoNurseRepository nurses = new MongoNurseRepository();
        MongoAdminRepository admins = new MongoAdminRepository();
        MongoAppointmentRepository appointmentRepo = new MongoAppointmentRepository();
        MongoMedicalRecordRepository recordRepo = new MongoMedicalRecordRepository();
        MongoPrescriptionRepository prescriptionRepo = new MongoPrescriptionRepository();
        MongoPaymentRepository paymentRepo = new MongoPaymentRepository();
        MongoInvoiceRepository invoiceRepo = new MongoInvoiceRepository();
        MongoEmergencyCaseRepository emergencyRepo = new MongoEmergencyCaseRepository();

        List<UserFactory> factories = List.of(
                new PatientFactory(),
                new DoctorFactory(),
                new NurseFactory(),
                new AdminFactory()
        );
        users = new UserManagementServiceImpl(factories, patients, doctors, nurses, admins);

        notifications = new NotificationServiceImpl(new com.healthsync.repository.mongo.MongoNotificationRepository());
        appointments = new AppointmentManagementService(appointmentRepo, notifications);
        records = new MedicalRecordServiceImpl(recordRepo);
        prescriptions = new PrescriptionServiceImpl(prescriptionRepo);
        emergency = new EmergencyServiceImpl(emergencyRepo);
        billing = new BillingServiceImpl(paymentRepo, appointmentRepo, invoiceRepo);

        ExternalPaymentGateway gateway = (reference, amount) -> true;
        payments = new PaymentGatewayAdapter(gateway);

        HealthcareFactory factory = new HospitalFactory();
        careEnvironment = new CareEnvironmentService(factory);

        authController = new AuthenticationController(new AuthenticationServiceImpl(users));
        patientController = new PatientController(new com.healthsync.service.PatientService() {
            @Override public com.healthsync.model.Patient register(com.healthsync.model.Patient patient) { return patients.save(patient); }
            @Override public java.util.Optional<com.healthsync.model.Patient> findPatient(String patientId) { return patients.findById(patientId); }
        });
        appointmentController = new AppointmentController(appointments);
        recordController = new MedicalRecordController(records);
    }

    public void shutdown() { database.close(); }
}