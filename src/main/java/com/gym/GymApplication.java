package com.gym;

import com.gym.dao.UserDAO;
import com.gym.dao.impl.UserDAOImpl;
import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Receptionist;
import com.gym.model.UserRole;
import com.gym.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GymApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        initializeDatabase();
        createDefaultUsers();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Gym & Fitness Center Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}