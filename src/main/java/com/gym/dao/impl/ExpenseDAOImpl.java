package com.gym.dao.impl;

import com.gym.dao.ExpenseDAO;
import com.gym.model.Expense;
import com.gym.model.ExpenseCategory;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseDAOImpl implements ExpenseDAO{
    private static final String INSERT_SQL =
            "INSERT INTO expenses (description, amount, date, category) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM expenses WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM expenses";
    private static final String SELECT_BY_DATE_RANGE_SQL =
            "SELECT * FROM expenses WHERE date BETWEEN ? AND ?";
    private static final String SELECT_BY_CATEGORY_SQL =
            "SELECT * FROM expenses WHERE category = ?";
    private static final String SELECT_BY_MONTH_SQL =
            "SELECT * FROM expenses WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
    private static final String SELECT_TOTAL_EXPENSES_BY_MONTH_SQL =
            "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE datetime(date/1000, 'unixepoch', 'localtime') LIKE ? || '%'";
    private static final String UPDATE_SQL =
            "UPDATE expenses SET description = ?, amount = ?, date = ?, category = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM expenses WHERE id = ?";

}
