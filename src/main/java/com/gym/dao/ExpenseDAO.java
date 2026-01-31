package com.gym.dao;

import com.gym.model.Expense;
import com.gym.model.ExpenseCategory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseDAO {
    Expense save(Expense expense);
    Optional<Expense> findById(int id);
    List<Expense> findAll();
    List<Expense> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Expense> findByCategory(ExpenseCategory category);
    List<Expense> findByMonth(int month, int year);
    double getTotalExpensesByMonth(int month, int year);
    Expense update(Expense expense);
    boolean delete(int id);
}
