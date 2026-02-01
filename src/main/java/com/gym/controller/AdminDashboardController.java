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

    private User currentUser;
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final ExpenseDAO expenseDAO = new ExpenseDAOImpl();
    private final FinancialService financialService = new FinancialService(paymentDAO, expenseDAO);

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome: " + user.getName());
        initializeComboBoxes();
        initializePaymentTableView();
        initializeExpenseTableView();
        refreshFinancialData();
        refreshPaymentsTable();
        refreshExpensesTable();
    }

    private void initializeComboBoxes() {
        expenseCategoryCombo.setItems(FXCollections.observableArrayList(ExpenseCategory.values()));

        reportMonthCombo.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));

        List<String> years = new java.util.ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            years.add(String.valueOf(i));
        }
        reportYearCombo.setItems(FXCollections.observableArrayList(years));
        reportYearCombo.setValue(String.valueOf(currentYear));

        LocalDate now = LocalDate.now();
        reportMonthCombo.setValue(getMonthName(now.getMonthValue()));
    }

    private String getMonthName(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "January";
        };
    }

    private int getMonthNumber(String monthName) {
        return switch (monthName) {
            case "January" -> 1;
            case "February" -> 2;
            case "March" -> 3;
            case "April" -> 4;
            case "May" -> 5;
            case "June" -> 6;
            case "July" -> 7;
            case "August" -> 8;
            case "September" -> 9;
            case "October" -> 10;
            case "November" -> 11;
            case "December" -> 12;
            default -> 1;
        };
    }

    private void initializePaymentTableView() {
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        paymentMemberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentDateColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
        );
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void initializeExpenseTableView() {
        expenseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expenseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        expenseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseDateColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
        );
        expenseCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    }
}
