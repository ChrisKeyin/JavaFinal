package com.chrisking.dao.jdbc;

import com.chrisking.dao.UserDAO;
import com.chrisking.model.Role;
import com.chrisking.model.User;
import com.chrisking.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserJdbcDAO implements UserDAO {

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, phone, address, role) VALUES (?,?,?,?,?,?) RETURNING user_id";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole().name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Create user failed: " + e.getMessage(), e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, email, phone, address, role FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setAddress(rs.getString("address"));
                    u.setRole(Role.valueOf(rs.getString("role")));
                    return u;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByUsername failed: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT user_id, username, password_hash, email, phone, address, role FROM users ORDER BY user_id";
        List<User> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setAddress(rs.getString("address"));
                u.setRole(Role.valueOf(rs.getString("role")));
                list.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean deleteById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("deleteById failed: " + e.getMessage(), e);
        }
    }
}
