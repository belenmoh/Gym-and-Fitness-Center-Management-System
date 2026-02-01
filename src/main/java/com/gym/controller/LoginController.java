package com.gym.controller;

import com.gym.dao.UserDAO;
import com.gym.dao.impl.UserDAOImpl;
import com.gym.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField; // Visible field
    @FXML private Button showPasswordButton;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAOImpl();
    private boolean isPasswordVisible = false;

    @FXML
    private void togglePassword() {
        if (isPasswordVisible) {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            showPasswordButton.setText("👁");
        } else {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            showPasswordButton.setText("🙈");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        // Get password from whichever field is currently active
        String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isEmpty()) {
            errorLabel.setText("No user found with username: '" + username + "'");
            return;
        }

        User user = userOpt.get();
        if (user.getPassword().equals(password)) {
            errorLabel.setText("");
            loadDashboard(user);
        } else {
            errorLabel.setText("Invalid password. Please try again.");
        }
    }

    private void loadDashboard(User user) {
        try {
            String fxmlPath = switch (user.getRole()) {
                case ADMIN -> "/fxml/AdminDashboard.fxml";
                case RECEPTIONIST -> "/fxml/ReceptionistDashboard.fxml";
                case MEMBER -> "/fxml/MemberDashboard.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Set user for the respective controller
            Object controller = loader.getController();
            if (controller instanceof com.gym.controller.AdminDashboardController c) c.setUser(user);
            else if (controller instanceof ReceptionistDashboardController c) c.setUser(user);
            else if (controller instanceof MemberDashboardController c) c.setUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gym & Fitness Center - " + user.getRole());
            stage.show();

        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard: " + e.getMessage());
        }
    }
}
