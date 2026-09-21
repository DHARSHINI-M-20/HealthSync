package com.healthsync.util;
import com.healthsync.model.User;
import java.util.Optional;
/** Singleton current-user context for desktop-session authorization. */
public final class SessionContext {
    private static final SessionContext INSTANCE = new SessionContext(); private User currentUser;
    private SessionContext() { }
    public static SessionContext getInstance() { return INSTANCE; }
    public Optional<User> getCurrentUser() { return Optional.ofNullable(currentUser); }
    public void setCurrentUser(User user) { currentUser=user; }
    public void clear() { currentUser=null; }
}
