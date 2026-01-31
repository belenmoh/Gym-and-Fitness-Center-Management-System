package com.gym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:gym_database.db";
    private static boolean initialized = false;

    private static void createUsersTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL CHECK (role IN ('MEMBER', 'RECEPTIONIST', 'ADMIN'))
            )
        """;
        stmt.execute(sql);
    }

    private static void createMembersTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS members (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                membership_type TEXT NOT NULL,
                start_date TEXT,
                end_date TEXT,
                FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
            )
        """;
        stmt.execute(sql);
    }

    private static void createPaymentsTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                member_id INTEGER NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                type TEXT NOT NULL CHECK (type IN ('MEMBERSHIP', 'CLASS', 'OTHER')),
                FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
            )
        """;
        stmt.execute(sql);
    }
}
