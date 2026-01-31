package com.gym.dao;

import com.gym.model.Booking;
import com.gym.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingDAO {
    Booking save(Booking booking);
    Optional<Booking> findById(int id);
    List<Booking> findAll();
    List<Booking> findByMemberId(int memberId);
    List<Booking> findByClassName(String className);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Booking update(Booking booking);
    boolean delete(int id);
}
