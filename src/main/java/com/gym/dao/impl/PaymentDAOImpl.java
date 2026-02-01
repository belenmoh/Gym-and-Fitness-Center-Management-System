package com.gym.dao.impl;

import com.gym.dao.PaymentDAO;
import com.gym.model.Payment;
import com.gym.model.PaymentType;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    private static final String INSERT_SQL =
            "INSERT INTO payments (member_id, amount, date, type) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM payments WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM payments";
    private static final String SELECT_BY_MEMBER_ID_SQL =
            "SELECT * FROM payments WHERE member_id = ?";
    private static final String SELECT_BY_DATE_RANGE_SQL =
            "SELECT * FROM payments WHERE date BETWEEN ? AND ?";
    private static final String SELECT_BY_TYPE_SQL =
            "SELECT * FROM payments WHERE type = ?";
    private static final String SELECT_BY_MONTH_SQL =
            "SELECT * FROM payments WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
    private static final String SELECT_TOTAL_INCOME_BY_MONTH_SQL =
            "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE datetime(date/1000, 'unixepoch', 'localtime') LIKE ? || '%'";
    private static final String UPDATE_SQL =
            "UPDATE payments SET member_id = ?, amount = ?, date = ?, type = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM payments WHERE id = ?";

    @Override
    public Payment save(Payment payment) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getMemberId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setDate(3, Date.valueOf(payment.getDate()));
            stmt.setString(4, payment.getType().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment", e);
        }
        return payment;
    }

    @Override
    public Optional<Payment> findById(int id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all payments", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findByMemberId(int memberId) {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MEMBER_ID_SQL)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by member id", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_DATE_RANGE_SQL)) {

            // Convert LocalDate to timestamp (milliseconds since epoch)
            long startTimestamp = startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endTimestamp = endDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(); // Add 1 day to include end date

            stmt.setLong(1, startTimestamp);
            stmt.setLong(2, endTimestamp);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by date range", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findByType(PaymentType type) {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_TYPE_SQL)) {

            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by type", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findByMonth(int month, int year) {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MONTH_SQL)) {

            stmt.setString(1, String.format("%02d", month));
            stmt.setString(2, String.valueOf(year));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by month", e);
        }
        return payments;
    }

    @Override
    public double getTotalIncomeByMonth(int month, int year) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TOTAL_INCOME_BY_MONTH_SQL)) {

            // Format as YYYY-MM pattern to match datetime format
            String datePattern = String.format("%04d-%02d", year, month);
            stmt.setString(1, datePattern);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total income by month", e);
        }
        return 0.0;
    }

    @Override
    public Payment update(Payment payment) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setInt(1, payment.getMemberId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setDate(3, Date.valueOf(payment.getDate()));
            stmt.setString(4, payment.getType().name());
            stmt.setInt(5, payment.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating payment failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment", e);
        }
        return payment;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting payment", e);
        }
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setMemberId(rs.getInt("member_id"));
        payment.setAmount(rs.getDouble("amount"));

        // Handle date parsing with timestamp conversion
        try {
            Date date = rs.getDate("date");
            if (date != null) {
                payment.setDate(date.toLocalDate());
            }
        } catch (Exception e) {
            // If date parsing fails, try to parse as timestamp
            try {
                long timestamp = rs.getLong("date");
                if (!rs.wasNull()) {
                    payment.setDate(new java.util.Date(timestamp).toInstant()
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                }
            } catch (Exception ex) {
                payment.setDate(null);
            }
        }

        String typeStr = rs.getString("type");
        payment.setType(PaymentType.valueOf(typeStr));

        return payment;
    }
}
