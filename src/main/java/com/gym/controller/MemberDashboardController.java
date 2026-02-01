package com.gym.controller;

import com.gym.dao.BookingDAO;
import com.gym.dao.MemberDAO;
import com.gym.dao.PaymentDAO;
import com.gym.dao.impl.BookingDAOImpl;
import com.gym.dao.impl.MemberDAOImpl;
import com.gym.dao.impl.PaymentDAOImpl;
import com.gym.model.*;
import com.gym.service.BookingService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MemberDashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label memberIdLabel;

    @FXML
    private Label memberNameLabel;

    @FXML
    private Label membershipTypeLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private Label membershipStatusLabel;

    @FXML
    private TextArea benefitsTextArea;

    @FXML
    private ComboBox<String> classNameCombo;

    @FXML
    private DatePicker classDatePicker;

    @FXML
    private ComboBox<String> classTimeCombo;

    @FXML
    private Button bookClassButton;

    @FXML
    private Label bookingMessageLabel;

    @FXML
    private TableView<Booking> bookingsTableView;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> classNameColumn;

    @FXML
    private TableColumn<Booking, String> classTimeColumn;

    @FXML
    private TableColumn<Booking, String> bookingTimeColumn;

    @FXML
    private TableColumn<Booking, String> bookingStatusColumn;

    @FXML
    private TableColumn<Booking, Void> bookingActionsColumn;

    @FXML
    private Button refreshBookingsButton;

    @FXML
    private TableView<Payment> paymentsTableView;

    @FXML
    private TableColumn<Payment, Integer> paymentIdColumn;

    @FXML
    private TableColumn<Payment, Double> paymentAmountColumn;

    @FXML
    private TableColumn<Payment, String> paymentDateColumn;

    @FXML
    private TableColumn<Payment, String> paymentTypeColumn;

    @FXML
    private Button refreshPaymentsButton;
}
