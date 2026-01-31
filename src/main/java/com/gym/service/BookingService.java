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

    public Booking bookClass(int memberId, String className, LocalDateTime classTime) {
        if (!memberDAO.findById(memberId).isPresent()) {
            throw new IllegalArgumentException("Member not found");
        }

        if (classTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book classes in the past");
        }

        if (hasConflictingBooking(memberId, classTime)) {
            throw new IllegalArgumentException("Member already has a booking at this time");
        }

        Booking booking = new Booking();
        booking.setMemberId(memberId);
        booking.setClassName(className);
        booking.setBookingTime(LocalDateTime.now());
        booking.setClassTime(classTime);
        booking.setStatus(BookingStatus.BOOKED);

        return bookingDAO.save(booking);
    }

    public Booking cancelBooking(int bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new IllegalArgumentException("Cannot cancel a booking that is not in BOOKED status");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingDAO.update(booking);
    }

    public Optional<Booking> findBookingById(int id) {
        return bookingDAO.findById(id);
    }

    public List<Booking> getBookingsByMember(int memberId) {
        return bookingDAO.findByMemberId(memberId);
    }

    public List<Booking> getBookingsByClass(String className) {
        return bookingDAO.findByClassName(className);
    }

    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingDAO.findByStatus(status);
    }

    public List<Booking> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingDAO.findByDateRange(startDate, endDate);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }
}
