package com.gym.model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private LocalDate date;
    private ExpenseCategory category;

    public Expense() {}

    public Expense(int id, String description, double amount, LocalDate date, ExpenseCategory category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
}
