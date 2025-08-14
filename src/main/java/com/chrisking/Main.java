package com.chrisking;

import com.chrisking.model.Role;
import com.chrisking.model.User;
import com.chrisking.service.UserService;
import com.chrisking.ui.MenuRouter;
import com.chrisking.util.Database;
import com.chrisking.util.AppLog;

import java.sql.Connection;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger log = AppLog.get(Main.class);

    public static void main(String[] args) {
        AppLog.init();
        log.info("Gym Manager starting up");

        System.out.println("Gym Manager has started!");

        try (Connection ignored = Database.getConnection()) {
            System.out.println("DB connection OK.");
            log.info("Database connection verified.");
        } catch (Exception e) {
            System.out.println("DB connection failed: " + e.getMessage());
            log.severe("DB connection failed: " + e.getMessage());
        }

        UserService userService = new UserService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> doRegister(sc, userService);
                case "2" -> doLogin(sc, userService);
                case "0" -> {
                    log.info("Exiting application.");
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void doRegister(Scanner sc, UserService userService) {
        try {
            System.out.println("\n-- Register --");
            System.out.print("Username: ");
            String username = sc.nextLine().trim();
            System.out.print("Role (ADMIN/TRAINER/MEMBER): ");
            String roleStr = sc.nextLine().trim().toUpperCase();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine().trim();
            System.out.print("Phone: ");
            String phone = sc.nextLine().trim();
            System.out.print("Address: ");
            String address = sc.nextLine().trim();

            Role role = Role.valueOf(roleStr);
            User u = userService.register(username, email, phone, address, role, password);
            System.out.println("Registered user id: " + u.getUserId());
            log.info("Registered user: username=" + username + ", role=" + role + ", id=" + u.getUserId());
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation: " + ex.getMessage());
            log.warning("Registration validation failed: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Registration failed: " + ex.getMessage());
            log.severe("Registration failed: " + ex.getMessage());
        }
    }

    private static void doLogin(Scanner sc, UserService userService) {
        try {
            System.out.println("\n-- Login --");
            System.out.print("Username: ");
            String username = sc.nextLine().trim();
            System.out.print("Password: ");
            String password = sc.nextLine();

            User u = userService.authenticate(username, password);
            if (u == null) {
                System.out.println("Invalid credentials.");
                log.warning("Login failed for username=" + username);
            } else {
                System.out.println("Welcome, " + u.getUsername() + " (" + u.getRole() + ")!");
                log.info("Login success for username=" + u.getUsername() + ", role=" + u.getRole());
                MenuRouter.dispatch(u, sc);
            }
        } catch (Exception ex) {
            System.out.println("Login failed: " + ex.getMessage());
            log.severe("Login error: " + ex.getMessage());
        }
    }
}
