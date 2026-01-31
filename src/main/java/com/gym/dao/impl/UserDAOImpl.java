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
}
