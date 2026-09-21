package com.healthsync.repository;
import com.healthsync.model.Doctor;
import java.util.List;
import java.util.Optional;
/** Persistence boundary for doctors and specialty searches. */
public interface DoctorRepository extends Repository<Doctor> { Optional<Doctor> findByEmail(String email); List<Doctor> findBySpecialtyCode(String specialtyCode); }
