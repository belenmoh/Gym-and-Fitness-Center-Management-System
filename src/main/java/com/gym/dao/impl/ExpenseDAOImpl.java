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

    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                expenses.add(mapResultSetToExpense(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all expenses", e);
        }
        return expenses;
    }
    @Override
    public List<Expense> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_DATE_RANGE_SQL)) {

            // Convert LocalDate to timestamp (milliseconds since epoch)
            long startTimestamp = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endTimestamp = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(); // Add 1 day to include end date

            stmt.setLong(1, startTimestamp);
            stmt.setLong(2, endTimestamp);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                expenses.add(mapResultSetToExpense(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding expenses by date range", e);
        }
        return expenses;
    }

    @Override
    public List<Expense> findByCategory(ExpenseCategory category) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CATEGORY_SQL)) {

            stmt.setString(1, category.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                expenses.add(mapResultSetToExpense(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding expenses by category", e);
        }
        return expenses;
    }

    @Override
    public List<Expense> findByMonth(int month, int year) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MONTH_SQL)) {

            stmt.setString(1, String.format("%02d", month));
            stmt.setString(2, String.valueOf(year));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                expenses.add(mapResultSetToExpense(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding expenses by month", e);
        }
        return expenses;
    }

    @Override
    public double getTotalExpensesByMonth(int month, int year) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TOTAL_EXPENSES_BY_MONTH_SQL)) {

            // Format as YYYY-MM pattern to match datetime format
            String datePattern = String.format("%04d-%02d", year, month);
            stmt.setString(1, datePattern);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total expenses by month", e);
        }
        return 0.0;
    }

    @Override
    public Expense update(Expense expense) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setDate(3, Date.valueOf(expense.getDate()));
            stmt.setString(4, expense.getCategory().name());
            stmt.setInt(5, expense.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating expense failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating expense", e);
        }
        return expense;
    }

}
