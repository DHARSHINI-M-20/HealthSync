package com.healthsync.repository;
import com.healthsync.model.Appointment;
import java.util.List;
public interface AppointmentRepository extends Repository<Appointment> { List<Appointment> findByPatientId(String patientId); List<Appointment> findByDoctorId(String doctorId); }
