package com.gym.model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int memberId;
    private String className;
    private LocalDateTime bookingTime;
    private LocalDateTime classTime;
    private BookingStatus status;

    public Booking() {}

    public Booking(int id, int memberId, String className, LocalDateTime bookingTime, LocalDateTime classTime) {
        this.id = id;
        this.memberId = memberId;
        this.className = className;
        this.bookingTime = bookingTime;
        this.classTime = classTime;
        this.status = BookingStatus.BOOKED;
    }
}
