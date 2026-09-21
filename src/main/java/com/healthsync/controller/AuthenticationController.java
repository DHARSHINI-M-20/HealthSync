package com.healthsync.controller;

import com.healthsync.model.User;
import com.healthsync.service.AuthenticationService;
import java.util.Optional;

/** Translates login view actions into authentication use cases. */
public class AuthenticationController {
    private final AuthenticationService service;
    public AuthenticationController(AuthenticationService service) { this.service = service; }

    public User login(String email, String password) {
        Optional<User> found = service.authenticate(email, password);
        return found.orElse(null);
    }

    public void logout() { service.logout(); }
}