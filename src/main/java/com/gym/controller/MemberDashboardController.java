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
    private User currentUser;
    private Member currentMember;
    private final MemberDAO memberDAO = new MemberDAOImpl();
    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final BookingService bookingService = new BookingService(bookingDAO, memberDAO);

    public void setUser(User user) {
        this.currentUser = user;
        this.welcomeLabel.setText("Welcome: " + user.getName());
        loadMemberData();
        initializeComboBoxes();
        initializeBookingTableView();
        initializePaymentTableView();
    }

    private void loadMemberData() {
        Optional<Member> memberOpt = memberDAO.findByUserId(currentUser.getId());
        if (memberOpt.isPresent()) {
            currentMember = memberOpt.get();
            displayMemberInfo();
            refreshBookingsTable();
            refreshPaymentsTable();
        }
    }

    private void displayMemberInfo() {
        if (currentMember != null) {
            memberIdLabel.setText(String.valueOf(currentMember.getMemberId()));
            memberNameLabel.setText(currentMember.getName());

            if (currentMember.getMembership() != null) {
                membershipTypeLabel.setText(currentMember.getMembership().getClass().getSimpleName());
                benefitsTextArea.setText(currentMember.getMembership().getBenefits());
            } else {
                membershipTypeLabel.setText("None");
                benefitsTextArea.setText("No active membership");
            }

            if (currentMember.getStartDate() != null) {
                startDateLabel.setText(currentMember.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                startDateLabel.setText("N/A");
            }

            if (currentMember.getEndDate() != null) {
                endDateLabel.setText(currentMember.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                endDateLabel.setText("N/A");
            }

            boolean isActive = currentMember.isMembershipActive();
            membershipStatusLabel.setText(isActive ? "Active" : "Expired");
            membershipStatusLabel.setStyle(isActive ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
        }
    }

    private void initializeComboBoxes() {
        classNameCombo.setItems(FXCollections.observableArrayList(
                "Yoga", "Pilates", "CrossFit", "Spinning", "Zumba", "Boxing", "Swimming", "Weight Training"
        ));

        classTimeCombo.setItems(FXCollections.observableArrayList(
                "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
        ));
    }

    private void initializeBookingTableView() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        classTimeColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getClassTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
        );
        bookingTimeColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getBookingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
        );
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        bookingActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                cancelButton.setOnAction(event -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleCancelBooking(booking);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking booking = getTableView().getItems().get(getIndex());
                    if (booking.getStatus() == BookingStatus.BOOKED) {
                        setGraphic(cancelButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void initializePaymentTableView() {
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentDateColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
        );
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
}
