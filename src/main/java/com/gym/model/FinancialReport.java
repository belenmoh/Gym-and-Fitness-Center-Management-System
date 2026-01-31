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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
        this.netCashFlow = this.totalIncome - this.totalExpenses;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
        this.netCashFlow = this.totalIncome - this.totalExpenses;
    }

    public double getNetCashFlow() {
        return netCashFlow;
    }

    @Override
    public String toString() {
        return "FinancialReport{" +
                "month=" + month +
                ", year=" + year +
                ", totalIncome=" + totalIncome +
                ", totalExpenses=" + totalExpenses +
                ", netCashFlow=" + netCashFlow +
                '}';
    }

}
