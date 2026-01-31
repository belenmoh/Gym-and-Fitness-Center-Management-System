package com.gym.model;

import java.time.LocalDate;

public class Payment {
    private int id;
    private int memberId;
    private double amount;
    private LocalDate date;
    private PaymentType type;

    public Payment() {}

    public Payment(int id, int memberId, double amount, LocalDate date, PaymentType type) {
        this.id = id;
        this.memberId = memberId;
        this.amount = amount;
        this.date = date;
        this.type = type;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }
}
