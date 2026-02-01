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

    @FXML
    public void handleRefreshFinancial() {
        refreshFinancialData();
    }

    @FXML
    public void handleRefreshPayments() {
        refreshPaymentsTable();
    }

    @FXML
    public void handleAddExpense() {
        try {
            String description = expenseDescriptionField.getText().trim();
            String amountStr = expenseAmountField.getText().trim();
            LocalDate date = expenseDatePicker.getValue();
            ExpenseCategory category = expenseCategoryCombo.getValue();

            if (description.isEmpty() || amountStr.isEmpty() || date == null || category == null) {
                expenseMessageLabel.setText("Please fill all fields");
                return;
            }

            double amount = Double.parseDouble(amountStr);

            Expense expense = new Expense();
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setDate(date);
            expense.setCategory(category);

            expenseDAO.save(expense);

            expenseMessageLabel.setText("Expense added successfully! ID: " + expense.getId());
            clearExpenseFields();
            refreshFinancialData();
            refreshExpensesTable();

        } catch (NumberFormatException e) {
            expenseMessageLabel.setText("Invalid amount format");
        } catch (Exception e) {
            expenseMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefreshExpenses() {
        refreshExpensesTable();
    }

    @FXML
    public void handleGenerateReport() {
        try {
            String monthName = reportMonthCombo.getValue();
            String yearStr = reportYearCombo.getValue();

            if (monthName == null || yearStr == null) {
                expenseMessageLabel.setText("Please select month and year");
                return;
            }

            int month = getMonthNumber(monthName);
            int year = Integer.parseInt(yearStr);

            FinancialReport report = financialService.getMonthlyReport(month, year);

            reportTitleLabel.setText("Financial Report - " + monthName + " " + year);
            reportIncomeLabel.setText(String.format("ETB %.2f", report.getTotalIncome()));
            reportExpensesLabel.setText(String.format("ETB %.2f", report.getTotalExpenses()));
            reportNetFlowLabel.setText(String.format("ETB %.2f", report.getNetCashFlow()));

            reportIncomeLabel.setStyle(report.getTotalIncome() >= 0 ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
            reportExpensesLabel.setStyle("-fx-text-fill: #e74c3c;");
            reportNetFlowLabel.setStyle(report.getNetCashFlow() >= 0 ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");

        } catch (NumberFormatException e) {
            expenseMessageLabel.setText("Invalid year format");
        } catch (Exception e) {
            expenseMessageLabel.setText("Error generating report: " + e.getMessage());
        }
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gym & Fitness Center - Login");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading login screen: " + e.getMessage());
        }
    }

    private void refreshFinancialData() {
        double totalIncome = financialService.getTotalIncome();
        double totalExpenses = financialService.getTotalExpenses();
        double netCashFlow = financialService.getNetCashFlow();

        totalIncomeLabel.setText(String.format("ETB %.2f", totalIncome));
        totalExpensesLabel.setText(String.format("ETB %.2f", totalExpenses));
        netCashFlowLabel.setText(String.format("ETB %.2f", netCashFlow));

        totalIncomeLabel.setStyle(totalIncome >= 0 ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
        totalExpensesLabel.setStyle("-fx-text-fill: #e74c3c;");
        netCashFlowLabel.setStyle(netCashFlow >= 0 ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
    }


    private void refreshPaymentsTable() {
        List<Payment> payments = financialService.getPaymentsByDateRange(
                LocalDate.now().minusMonths(12), LocalDate.now()
        );
        ObservableList<Payment> paymentList = FXCollections.observableArrayList(payments);
        paymentsTableView.setItems(paymentList);
    }


    private void refreshExpensesTable() {
        List<Expense> expenses = financialService.getExpensesByDateRange(
                LocalDate.now().minusMonths(12), LocalDate.now()
        );
        ObservableList<Expense> expenseList = FXCollections.observableArrayList(expenses);
        expensesTableView.setItems(expenseList);
    }


    private void clearExpenseFields() {
        expenseDescriptionField.clear();
        expenseAmountField.clear();
        expenseDatePicker.setValue(null);
        expenseCategoryCombo.getSelectionModel().clearSelection();
    }
}

