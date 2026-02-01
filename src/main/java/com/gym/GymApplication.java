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
    private void initializeDatabase() {
        try {
            DatabaseUtil.getConnection();
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    private void createDefaultUsers() {
        UserDAO userDAO = new UserDAOImpl();

        try {
            if (!userDAO.existsByUsername("admin")) {
                Admin admin = new Admin();
                admin.setName("System Administrator");
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setAdminId(1);
                userDAO.save(admin);
                System.out.println("Default admin user created: admin/admin123");
            }

            if (!userDAO.existsByUsername("receptionist")) {
                Receptionist receptionist = new Receptionist();
                receptionist.setName("John Receptionist");
                receptionist.setUsername("receptionist");
                receptionist.setPassword("rec123");
                receptionist.setEmployeeId(1);
                userDAO.save(receptionist);
                System.out.println("Default receptionist user created: receptionist/rec123");
            }

            if (!userDAO.existsByUsername("member")) {
                Member member = new Member();
                member.setName("Jane Member");
                member.setUsername("member");
                member.setPassword("mem123");
                userDAO.save(member);
                System.out.println("Default member user created: member/mem123");
            }

        } catch (Exception e) {
            System.err.println("Error creating default users: " + e.getMessage());
        }
    }
}