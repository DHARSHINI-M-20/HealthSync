package com.healthsync.repository;
import com.healthsync.model.Admin;
import java.util.Optional;
/** Persistence boundary for administrative accounts. */
public interface AdminRepository extends Repository<Admin> { Optional<Admin> findByEmail(String email); }
