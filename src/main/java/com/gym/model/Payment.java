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
}
