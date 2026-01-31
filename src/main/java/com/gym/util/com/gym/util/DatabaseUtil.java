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

    private static void createExpensesTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                category TEXT NOT NULL CHECK (category IN ('SALARY', 'EQUIPMENT', 'MAINTENANCE', 'UTILITIES', 'MARKETING', 'OTHER'))
            )
        """;
        stmt.execute(sql);
    }

    private static void createBookingsTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                member_id INTEGER NOT NULL,
                class_name TEXT NOT NULL,
                booking_time TEXT NOT NULL,
                class_time TEXT NOT NULL,
                status TEXT NOT NULL CHECK (status IN ('BOOKED', 'CANCELLED', 'COMPLETED', 'NO_SHOW')),
                FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
            )
        """;
        stmt.execute(sql);
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            createUsersTable(stmt);
            createMembersTable(stmt);
            createPaymentsTable(stmt);
            createExpensesTable(stmt);
            createBookingsTable(stmt);

            initialized = true;
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initializeDatabase();
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
