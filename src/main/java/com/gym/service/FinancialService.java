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
}
