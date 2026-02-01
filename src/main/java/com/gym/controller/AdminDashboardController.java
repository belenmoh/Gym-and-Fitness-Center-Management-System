package com.gym.controller;

import com.gym.dao.ExpenseDAO;
import com.gym.dao.PaymentDAO;
import com.gym.dao.impl.ExpenseDAOImpl;
import com.gym.dao.impl.PaymentDAOImpl;
import com.gym.model.*;
import com.gym.service.FinancialService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpensesLabel;

    @FXML
    private Label netCashFlowLabel;

    @FXML
    private Label profitLossLabel;

    @FXML
    private Label suggestionLabel;

    @FXML
    private Button refreshFinancialButton;

    @FXML
    private TableView<Payment> paymentsTableView;

    @FXML
    private TableColumn<Payment, Integer> paymentIdColumn;

    @FXML
    private TableColumn<Payment, Integer> paymentMemberIdColumn;

    @FXML
    private TableColumn<Payment, Double> paymentAmountColumn;

    @FXML
    private TableColumn<Payment, String> paymentDateColumn;

    @FXML
    private TableColumn<Payment, String> paymentTypeColumn;

    @FXML
    private Button refreshPaymentsButton;

    @FXML
    private TextField expenseDescriptionField;

    @FXML
    private TextField expenseAmountField;

    @FXML
    private DatePicker expenseDatePicker;

    @FXML
    private ComboBox<ExpenseCategory> expenseCategoryCombo;

    @FXML
    private Button addExpenseButton;

    @FXML
    private Label expenseMessageLabel;

    @FXML
    private TableView<Expense> expensesTableView;

    @FXML
    private TableColumn<Expense, Integer> expenseIdColumn;

    @FXML
    private TableColumn<Expense, String> expenseDescriptionColumn;

    @FXML
    private TableColumn<Expense, Double> expenseAmountColumn;

    @FXML
    private TableColumn<Expense, String> expenseDateColumn;

    @FXML
    private TableColumn<Expense, String> expenseCategoryColumn;

    @FXML
    private Button refreshExpensesButton;

    @FXML
    private ComboBox<String> reportMonthCombo;

    @FXML
    private ComboBox<String> reportYearCombo;

    @FXML
    private Button generateReportButton;

    @FXML
    private Label reportTitleLabel;

    @FXML
    private Label reportIncomeLabel;

    @FXML
    private Label reportExpensesLabel;

    @FXML
    private Label reportNetFlowLabel;
}
