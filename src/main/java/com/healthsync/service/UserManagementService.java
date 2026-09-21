package com.healthsync.service;
import com.healthsync.model.Role;
import com.healthsync.model.User;
import java.util.Optional;
/** Business boundary for user registration and role-aware retrieval. */
public interface UserManagementService { User register(UserRegistrationRequest request); Optional<User> findById(Role role, String id); Optional<User> findByEmail(Role role, String email); }
