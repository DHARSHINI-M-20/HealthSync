package com.healthsync.repository;
import com.healthsync.model.Nurse;
import java.util.List;
import java.util.Optional;
/** Persistence boundary for nursing staff. */
public interface NurseRepository extends Repository<Nurse> { Optional<Nurse> findByEmail(String email); List<Nurse> findByDepartment(String department); }
