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
}
