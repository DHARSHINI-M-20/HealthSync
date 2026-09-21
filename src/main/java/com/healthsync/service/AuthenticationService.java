package com.healthsync.service;
import com.healthsync.model.User;
import java.util.Optional;
/** Application use cases for authentication. */
public interface AuthenticationService { Optional<User> authenticate(String email, String password); void logout(); }
