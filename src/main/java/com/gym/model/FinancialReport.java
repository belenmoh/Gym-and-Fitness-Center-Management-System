package com.gym.model;

public class FinancialReport {
    private int month;
    private int year;
    private double totalIncome;
    private double totalExpenses;
    private double netCashFlow;

    public FinancialReport() {}

    public FinancialReport(int month, int year, double totalIncome, double totalExpenses) {
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netCashFlow = totalIncome - totalExpenses;
    }
}
