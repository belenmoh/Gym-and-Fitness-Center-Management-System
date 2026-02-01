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

    @FXML
    public void handleRegisterMember() {
        try {
            String name = memberNameField.getText().trim();
            String username = memberUsernameField.getText().trim();
            String password = memberPasswordField.getText().trim();
            String membershipTypeStr = membershipTypeCombo.getValue();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() ||
                    membershipTypeStr == null) {
                registerMessageLabel.setText("Please fill all fields");
                registerMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            // Get price from the label instead of input field
            double price = getMembershipPrice(membershipTypeStr);
            Membership membership = membershipService.createMembership(membershipTypeStr, price);

            Member member = new Member();
            member.setName(name);
            member.setUsername(username);
            member.setPassword(password);
            member.setMembership(membership);
            member.setStartDate(LocalDate.now());
            member.setEndDate(LocalDate.now().plusMonths(membership.getDurationMonths()));

            Member registeredMember = membershipService.registerMember(member);

            registerMessageLabel.setText("Member registered successfully! ID: " + registeredMember.getMemberId());
            clearRegistrationFields();
            refreshMembersTable();

        } catch (NumberFormatException e) {
            registerMessageLabel.setText("Invalid price format");
        } catch (Exception e) {
            registerMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleRenewMembership() {
        try {
            String memberIdStr = renewMemberIdField.getText().trim();
            String membershipTypeStr = renewMembershipCombo.getValue();
            String priceStr = renewPriceField.getText().trim();

            if (memberIdStr.isEmpty() || membershipTypeStr == null || priceStr.isEmpty()) {
                renewMessageLabel.setText("Please fill all fields");
                return;
            }

            int memberId = Integer.parseInt(memberIdStr);
            double price = Double.parseDouble(priceStr);
            Membership newMembership = membershipService.createMembership(membershipTypeStr, price);

            Member updatedMember = membershipService.renewMembership(memberId, newMembership);

            renewMessageLabel.setText("Membership renewed successfully!");
            clearRenewalFields();
            refreshMembersTable();

        } catch (NumberFormatException e) {
            renewMessageLabel.setText("Invalid member ID or price format");
        } catch (Exception e) {
            renewMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleRecordPayment() {
        try {
            String memberIdStr = paymentMemberIdField.getText().trim();
            String amountStr = paymentAmountField.getText().trim();
            PaymentType paymentType = paymentTypeCombo.getValue();

            if (memberIdStr.isEmpty() || amountStr.isEmpty() || paymentType == null) {
                paymentMessageLabel.setText("Please fill all fields");
                return;
            }

            int memberId = Integer.parseInt(memberIdStr);
            double amount = Double.parseDouble(amountStr);

            Payment payment = switch (paymentType) {
                case MEMBERSHIP -> billingService.recordMembershipPayment(memberId, amount);
                case CLASS -> billingService.recordClassPayment(memberId, amount);
                case OTHER -> billingService.recordOtherPayment(memberId, amount);
            };

            paymentMessageLabel.setText("Payment recorded successfully! ID: " + payment.getId());
            clearPaymentFields();

        } catch (NumberFormatException e) {
            paymentMessageLabel.setText("Invalid member ID or amount format");
        } catch (Exception e) {
            paymentMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefreshMembers() {
        refreshMembersTable();
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

    private void refreshMembersTable() {
        try {
            List<Member> members = membershipService.getAllMembers();
            ObservableList<Member> memberList = FXCollections.observableArrayList(members);
            membersTableView.setItems(memberList);
        } catch (Exception e) {
            System.err.println("Error loading members table: " + e.getMessage());
            // Set empty list to prevent UI crash
            membersTableView.setItems(FXCollections.observableArrayList());
        }
    }

    private void clearRegistrationFields() {
        memberNameField.clear();
        memberUsernameField.clear();
        memberPasswordField.clear();
        membershipTypeCombo.getSelectionModel().clearSelection();
        membershipPriceLabel.setText("ETB 0.00");
    }

    private void clearRenewalFields() {
        renewMemberIdField.clear();
        renewMembershipCombo.getSelectionModel().clearSelection();
        renewPriceField.clear();
    }

    private void clearPaymentFields() {
        paymentMemberIdField.clear();
        paymentAmountField.clear();
        paymentTypeCombo.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleCancelMembership() {
        Member selectedMember = membersTableView.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            cancelMembershipMessageLabel.setText("Please select a member to cancel membership");
            cancelMembershipMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Cancel Membership");
        confirmDialog.setHeaderText("Cancel Membership for " + selectedMember.getName());
        confirmDialog.setContentText("Are you sure you want to cancel the membership for " + selectedMember.getName() + "?\n\nThis action cannot be undone.");

        if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                // Set membership end date to today (effectively cancelling)
                Member updatedMember = new Member();
                updatedMember.setId(selectedMember.getId());
                updatedMember.setMemberId(selectedMember.getMemberId());
                updatedMember.setName(selectedMember.getName());
                updatedMember.setUsername(selectedMember.getUsername());
                updatedMember.setPassword(selectedMember.getPassword());
                updatedMember.setMembership(selectedMember.getMembership());
                updatedMember.setStartDate(selectedMember.getStartDate());
                updatedMember.setEndDate(LocalDate.now()); // Set to today to cancel

                memberDAO.update(updatedMember);

                cancelMembershipMessageLabel.setText("Membership cancelled successfully for " + selectedMember.getName());
                cancelMembershipMessageLabel.setStyle("-fx-text-fill: #27ae60;");

                // Refresh the members table
                refreshMembersTable();

            } catch (Exception e) {
                cancelMembershipMessageLabel.setText("Error cancelling membership: " + e.getMessage());
                cancelMembershipMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        }
    }
}
