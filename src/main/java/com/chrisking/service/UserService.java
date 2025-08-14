package com.chrisking.service;

import com.chrisking.dao.UserDAO;
import com.chrisking.dao.jdbc.UserJdbcDAO;
import com.chrisking.model.Role;
import com.chrisking.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.logging.Logger;
import com.chrisking.util.AppLog;

public class UserService {
    private final UserDAO userDAO = new UserJdbcDAO();
    private static final Logger log = AppLog.get(UserService.class);

    public User register(String username, String email, String phone, String address,
                         Role role, String plainPassword) {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username is required.");
        if (plainPassword == null || plainPassword.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        if (userDAO.findByUsername(username) != null)
            throw new IllegalArgumentException("Username already taken.");

        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(hash);
        u.setEmail(email);
        u.setPhone(phone);
        u.setAddress(address);
        u.setRole(role);

        User saved = userDAO.create(u);
        log.info("User created: username=" + username + ", role=" + role + ", id=" + saved.getUserId());
        return saved;
    }

    public User authenticate(String username, String plainPassword) {
        User u = userDAO.findByUsername(username);
        if (u == null) return null;
        boolean ok = BCrypt.checkpw(plainPassword, u.getPasswordHash());
        return ok ? u : null;
    }

    public List<User> listAllUsers() {
        return userDAO.findAll();
    }

    public boolean deleteUser(int requesterId, int targetUserId) {
        if (requesterId == targetUserId) {
            throw new IllegalArgumentException("You cannot delete your own account.");
        }
        boolean ok = userDAO.deleteById(targetUserId);
        log.info("Admin deleteUser: requesterId=" + requesterId + ", targetUserId=" + targetUserId + ", success=" + ok);
        return ok;
    }
}
