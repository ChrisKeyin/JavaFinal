package com.chrisking.dao;

import com.chrisking.model.User;
import java.util.List;

public interface UserDAO {
    User create(User user);
    User findByUsername(String username);
    List<User> findAll();
    boolean deleteById(int userId);
}
