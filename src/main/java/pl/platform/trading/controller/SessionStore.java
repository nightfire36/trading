package pl.platform.trading.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.platform.trading.sql.user.User;

@Component
@SessionScope
public class SessionStore {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
