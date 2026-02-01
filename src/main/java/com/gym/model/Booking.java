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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
    public LocalDateTime getClassTime() {
        return classTime;
    }
    public void setClassTime(LocalDateTime classTime) {
        this.classTime = classTime;
    }
    public BookingStatus getStatus() {
        return status;
    }
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", className='" + className + '\'' +
                ", bookingTime=" + bookingTime +
                ", classTime=" + classTime +
                ", status=" + status +
                '}';
    }
}
