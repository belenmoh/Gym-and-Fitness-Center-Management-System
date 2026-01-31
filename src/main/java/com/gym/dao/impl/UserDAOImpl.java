package com.gym.dao.impl;

import com.gym.dao.UserDAO;
import com.gym.model.User;
import com.gym.model.UserRole;
import com.gym.model.Member;
import com.gym.model.Receptionist;
import com.gym.model.Admin;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO{
    private static final String INSERT_SQL =
            "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_BY_USERNAME_SQL =
            "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM users";
    private static final String UPDATE_SQL =
            "UPDATE users SET name = ?, username = ?, password = ?, role = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM users WHERE id = ?";
    private static final String EXISTS_BY_USERNAME_SQL =
            "SELECT COUNT(*) FROM users WHERE username = ?";

    @Override
    public User save(User user) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }
        return Optional.empty();
    }
}
