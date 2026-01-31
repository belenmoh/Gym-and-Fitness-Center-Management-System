package com.gym.service;

import com.gym.dao.PaymentDAO;
import com.gym.dao.ExpenseDAO;
import com.gym.model.FinancialReport;
import com.gym.model.Payment;
import com.gym.model.Expense;

import java.time.LocalDate;
import java.util.List;

public class FinancialService {
    private final PaymentDAO paymentDAO;
    private final ExpenseDAO expenseDAO;

    public FinancialService(PaymentDAO paymentDAO, ExpenseDAO expenseDAO) {
        this.paymentDAO = paymentDAO;
        this.expenseDAO = expenseDAO;
    }

    public double getTotalIncome() {
        return paymentDAO.findAll().stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        return expenseDAO.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getNetCashFlow() {
        return getTotalIncome() - getTotalExpenses();
    }

    public double getTotalIncomeByMonth(int month, int year) {
        return paymentDAO.getTotalIncomeByMonth(month, year);
    }

    public double getTotalExpensesByMonth(int month, int year) {
        return expenseDAO.getTotalExpensesByMonth(month, year);
    }

    public double getNetCashFlowByMonth(int month, int year) {
        return getTotalIncomeByMonth(month, year) - getTotalExpensesByMonth(month, year);
    }

    public FinancialReport getMonthlyReport(int month, int year) {
        double totalIncome = getTotalIncomeByMonth(month, year);
        double totalExpenses = getTotalExpensesByMonth(month, year);
        return new FinancialReport(month, year, totalIncome, totalExpenses);
    }

    public List<FinancialReport> getYearlyReport(int year) {
        List<FinancialReport> reports = new java.util.ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            reports.add(getMonthlyReport(month, year));
        }
        return reports;
    }

    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentDAO.findByDateRange(startDate, endDate);
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseDAO.findByDateRange(startDate, endDate);
    }

    public List<Payment> getPaymentsByMonth(int month, int year) {
        return paymentDAO.findByMonth(month, year);
    }

    public List<Expense> getExpensesByMonth(int month, int year) {
        return expenseDAO.findByMonth(month, year);
    }

    public double getAverageMonthlyIncome(int year) {
        double totalYearlyIncome = 0;
        for (int month = 1; month <= 12; month++) {
            totalYearlyIncome += getTotalIncomeByMonth(month, year);
        }
        return totalYearlyIncome / 12;
    }

    public double getAverageMonthlyExpenses(int year) {
        double totalYearlyExpenses = 0;
        for (int month = 1; month <= 12; month++) {
            totalYearlyExpenses += getTotalExpensesByMonth(month, year);
        }
        return totalYearlyExpenses / 12;
    }

    public int getProfitableMonthsCount(int year) {
        int count = 0;
        for (int month = 1; month <= 12; month++) {
            if (getNetCashFlowByMonth(month, year) > 0) {
                count++;
            }
        }
        return count;
    }

    public int getLossMonthsCount(int year) {
        int count = 0;
        for (int month = 1; month <= 12; month++) {
            if (getNetCashFlowByMonth(month, year) < 0) {
                count++;
            }
        }
        return count;
    }
}
