package com.gym.service;

import com.gym.dao.BookingDAO;
import com.gym.dao.MemberDAO;
import com.gym.model.Booking;
import com.gym.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private final BookingDAO bookingDAO;
    private final MemberDAO memberDAO;

    public BookingService(BookingDAO bookingDAO, MemberDAO memberDAO) {
        this.bookingDAO = bookingDAO;
        this.memberDAO = memberDAO;
    }
}
