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
}
