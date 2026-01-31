package com.gym.dao.impl;

import com.gym.dao.BookingDAO;
import com.gym.model.Booking;
import com.gym.model.BookingStatus;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAOImpl implements BookingDAO {

    private static final String INSERT_SQL =
            "INSERT INTO bookings (member_id, class_name, booking_time, class_time, status) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM bookings WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM bookings";
    private static final String SELECT_BY_MEMBER_ID_SQL =
            "SELECT * FROM bookings WHERE member_id = ?";
    private static final String SELECT_BY_CLASS_NAME_SQL =
            "SELECT * FROM bookings WHERE class_name = ?";
    private static final String SELECT_BY_STATUS_SQL =
            "SELECT * FROM bookings WHERE status = ?";
    private static final String SELECT_BY_DATE_RANGE_SQL =
            "SELECT * FROM bookings WHERE class_time BETWEEN ? AND ?";
    private static final String UPDATE_SQL =
            "UPDATE bookings SET member_id = ?, class_name = ?, booking_time = ?, class_time = ?, status = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM bookings WHERE id = ?";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Booking save(Booking booking) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getMemberId());
            stmt.setString(2, booking.getClassName());
            stmt.setString(3, booking.getBookingTime().format(DATE_TIME_FORMATTER));
            stmt.setString(4, booking.getClassTime().format(DATE_TIME_FORMATTER));
            stmt.setString(5, booking.getStatus().name());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving booking", e);
        }
        return booking;
    }

    @Override
    public Optional<Booking> findById(int id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding booking by id", e);
        }
        return Optional.empty();
    }
}
