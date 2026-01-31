package com.gym.dao.impl;

import com.gym.dao.ExpenseDAO;
import com.gym.model.Expense;
import com.gym.model.ExpenseCategory;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseDAOImpl implements ExpenseDAO{
    private static final String INSERT_SQL =
            "INSERT INTO expenses (description, amount, date, category) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM expenses WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM expenses";
    private static final String SELECT_BY_DATE_RANGE_SQL =
            "SELECT * FROM expenses WHERE date BETWEEN ? AND ?";
    private static final String SELECT_BY_CATEGORY_SQL =
            "SELECT * FROM expenses WHERE category = ?";
    private static final String SELECT_BY_MONTH_SQL =
            "SELECT * FROM expenses WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
    private static final String SELECT_TOTAL_EXPENSES_BY_MONTH_SQL =
            "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE datetime(date/1000, 'unixepoch', 'localtime') LIKE ? || '%'";
    private static final String UPDATE_SQL =
            "UPDATE expenses SET description = ?, amount = ?, date = ?, category = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM expenses WHERE id = ?";

    @Override
    public Expense save(Expense expense) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setDate(3, Date.valueOf(expense.getDate()));
            stmt.setString(4, expense.getCategory().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    expense.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving expense", e);
        }
        return expense;
    }

    @Override
    public Optional<Expense> findById(int id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToExpense(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding expense by id", e);
        }
        return Optional.empty();
    }
}
