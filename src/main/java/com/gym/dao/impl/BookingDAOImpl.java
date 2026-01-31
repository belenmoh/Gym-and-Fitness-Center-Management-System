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
}
