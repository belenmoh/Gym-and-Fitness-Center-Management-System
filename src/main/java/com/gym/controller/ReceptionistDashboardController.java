package com.gym.controller;

import com.gym.dao.MemberDAO;
import com.gym.dao.PaymentDAO;
import com.gym.dao.UserDAO;
import com.gym.dao.impl.MemberDAOImpl;
import com.gym.dao.impl.PaymentDAOImpl;
import com.gym.dao.impl.UserDAOImpl;
import com.gym.model.*;
import com.gym.service.BillingService;
import com.gym.service.MembershipService;
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

public class ReceptionistDashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField memberNameField;

    @FXML
    private TextField memberUsernameField;

    @FXML
    private PasswordField memberPasswordField;

    @FXML
    private ComboBox<String> membershipTypeCombo;

    @FXML
    private Label membershipPriceLabel;

    @FXML
    private Button registerMemberButton;

    @FXML
    private Label registerMessageLabel;

    @FXML
    private TextField renewMemberIdField;

    @FXML
    private ComboBox<String> renewMembershipCombo;

    @FXML
    private TextField renewPriceField;

    @FXML
    private Button renewMembershipButton;

    @FXML
    private Label renewMessageLabel;

    @FXML
    private TextField paymentMemberIdField;

    @FXML
    private TextField paymentAmountField;

    @FXML
    private ComboBox<PaymentType> paymentTypeCombo;

    @FXML
    private Button recordPaymentButton;

    @FXML
    private Label paymentMessageLabel;

    @FXML
    private TableView<Member> membersTableView;

    @FXML
    private TableColumn<Member, Integer> memberIdColumn;

    @FXML
    private TableColumn<Member, String> memberNameColumn;

    @FXML
    private TableColumn<Member, String> memberUsernameColumn;

    @FXML
    private TableColumn<Member, String> memberMembershipColumn;

    @FXML
    private TableColumn<Member, String> memberEndDateColumn;

    @FXML
    private Button refreshMembersButton;

    @FXML
    private Button cancelMembershipButton;

    @FXML
    private Label cancelMembershipMessageLabel;

    private User currentUser;
    private final UserDAO userDAO = new UserDAOImpl();
    private final MemberDAO memberDAO = new MemberDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final MembershipService membershipService = new MembershipService(memberDAO, userDAO);
    private final BillingService billingService = new BillingService(paymentDAO, membershipService);

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome: " + user.getName());
        initializeComboBoxes();
        initializeTableView();
    }

    private void initializeComboBoxes() {
        membershipTypeCombo.setItems(FXCollections.observableArrayList(
                "Monthly", "Annual", "VIP"
        ));

        // Add listener to update price when membership type changes
        membershipTypeCombo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> updateMembershipPrice(newValue)
        );

        renewMembershipCombo.setItems(FXCollections.observableArrayList(
                "Monthly", "Annual", "VIP"
        ));

        paymentTypeCombo.setItems(FXCollections.observableArrayList(
                PaymentType.MEMBERSHIP, PaymentType.CLASS, PaymentType.OTHER
        ));
    }

    private void updateMembershipPrice(String membershipType) {
        double price = getMembershipPrice(membershipType);
        membershipPriceLabel.setText(String.format("ETB %.2f", price));
    }

    private double getMembershipPrice(String membershipType) {
        if (membershipType == null) return 0.0;

        switch (membershipType) {
            case "Monthly":
                return 2500.0;   // 2500 ETB for monthly
            case "Annual":
                return 25000.0;  // 25000 ETB for annual (good discount)
            case "VIP":
                return 50000.0;  // 50000 ETB for VIP
            default:
                return 0.0;
        }
    }

    private void initializeTableView() {
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        memberUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        memberMembershipColumn.setCellValueFactory(cellData -> {
            Membership membership = cellData.getValue().getMembership();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> membership != null ? membership.getClass().getSimpleName() : "None"
            );
        });
        memberEndDateColumn.setCellValueFactory(cellData -> {
            LocalDate endDate = cellData.getValue().getEndDate();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> endDate != null ? endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A"
            );
        });

        refreshMembersTable();
    }
}
