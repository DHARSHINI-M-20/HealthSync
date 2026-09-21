package com.healthsync.util;
import org.mindrot.jbcrypt.BCrypt;
/** Password hashing boundary; no raw passwords are persisted. */
public final class PasswordHasher {
    private PasswordHasher() { }
    public static String hash(String password) { return BCrypt.hashpw(password, BCrypt.gensalt()); }
    public static boolean matches(String password, String hash) { return BCrypt.checkpw(password, hash); }
}
