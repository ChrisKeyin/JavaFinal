package com.chrisking.ui;

import com.chrisking.model.Membership;
import com.chrisking.model.User;
import com.chrisking.model.WorkoutClass;
import com.chrisking.service.GymMerchService;
import com.chrisking.service.MembershipService;
import com.chrisking.service.UserService;
import com.chrisking.service.WorkoutClassService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public final class MenuRouter {
    private MenuRouter() {}

    private static final DateTimeFormatter INPUT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void dispatch(User user, Scanner sc) {
        switch (user.getRole()) {
            case ADMIN   -> adminMenu(sc, user);
            case TRAINER -> trainerMenu(sc, user);
            case MEMBER  -> memberMenu(sc, user);
        }
    }

    private static void adminMenu(Scanner sc, User user) {
        MembershipService membershipService = new MembershipService();
        GymMerchService merchService = new GymMerchService();
        UserService userService = new UserService();

        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1) View all users");
            System.out.println("2) Delete a user");
            System.out.println("3) View memberships & total revenue");
            System.out.println("4) Manage merch");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    var users = userService.listAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("(no users found)");
                    } else {
                        System.out.println("-- Users --");
                        System.out.printf("%-4s %-16s %-8s %-28s %-16s %s%n",
                                "ID", "Username", "Role", "Email", "Phone", "Address");
                        for (var u : users) {
                            System.out.printf("%-4d %-16s %-8s %-28s %-16s %s%n",
                                    u.getUserId(),
                                    u.getUsername(),
                                    u.getRole(),
                                    nullSafe(u.getEmail()),
                                    nullSafe(u.getPhone()),
                                    nullSafe(u.getAddress()));
                        }
                    }
                }
                case "2" -> {
                    try {
                        System.out.print("Enter user ID to delete: ");
                        int targetId = Integer.parseInt(sc.nextLine().trim());
                        boolean ok = userService.deleteUser(user.getUserId(), targetId);
                        System.out.println(ok ? "User deleted." : "No user deleted (check ID).");
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid ID.");
                    } catch (IllegalArgumentException iae) {
                        System.out.println("Cannot delete: " + iae.getMessage());
                    } catch (Exception e) {
                        System.out.println("Delete failed: " + e.getMessage());
                    }
                }
                case "3" -> {
                    var all = membershipService.getAll();
                    System.out.println("-- All memberships --");
                    if (all.isEmpty()) System.out.println("(none yet)");
                    else all.forEach(System.out::println);
                    double total = membershipService.getTotalRevenue();
                    System.out.printf("Total revenue: $%.2f%n", total);
                }
                case "4" -> merchAdminMenu(sc, merchService);
                case "0" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void merchAdminMenu(Scanner sc, GymMerchService merchService) {
        while (true) {
            System.out.println("\n--- Merch Management ---");
            System.out.println("1) Add item");
            System.out.println("2) Update price");
            System.out.println("3) Update quantity");
            System.out.println("4) List all items");
            System.out.println("5) Total stock value");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Name: ");
                        String name = sc.nextLine().trim();
                        System.out.print("Type (e.g., Drink/Gear/Food): ");
                        String type = sc.nextLine().trim();
                        System.out.print("Price (e.g., 3.50): ");
                        BigDecimal price = new BigDecimal(sc.nextLine().trim());
                        System.out.print("Quantity: ");
                        int qty = Integer.parseInt(sc.nextLine().trim());
                        var saved = merchService.addItem(name, type, price, qty);
                        System.out.println("Added: " + saved);
                    }
                    case "2" -> {
                        System.out.print("Merch ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("New price: ");
                        BigDecimal price = new BigDecimal(sc.nextLine().trim());
                        boolean ok = merchService.setPrice(id, price);
                        System.out.println(ok ? "Price updated." : "No item updated (check ID).");
                    }
                    case "3" -> {
                        System.out.print("Merch ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("New quantity: ");
                        int qty = Integer.parseInt(sc.nextLine().trim());
                        boolean ok = merchService.setQuantity(id, qty);
                        System.out.println(ok ? "Quantity updated." : "No item updated (check ID).");
                    }
                    case "4" -> {
                        var items = merchService.listAll();
                        if (items.isEmpty()) System.out.println("(no merch yet)");
                        else items.forEach(System.out::println);
                    }
                    case "5" -> {
                        var total = merchService.totalStockValue();
                        System.out.println("Total stock value: $" + total);
                    }
                    case "0" -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number.");
            } catch (Exception e) {
                System.out.println("Action failed: " + e.getMessage());
            }
        }
    }

    private static void trainerMenu(Scanner sc, User user) {
        MembershipService membershipService = new MembershipService();
        WorkoutClassService classService = new WorkoutClassService();
        GymMerchService merchService = new GymMerchService();

        while (true) {
            System.out.println("\n=== Trainer Menu ===");
            System.out.println("1) Create a workout class");
            System.out.println("2) Update a workout class");
            System.out.println("3) Delete a workout class");
            System.out.println("4) View my classes");
            System.out.println("5) Purchase membership");
            System.out.println("6) View merch list");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> createClassFlow(sc, classService, user.getUserId());
                case "2" -> updateClassFlow(sc, classService, user.getUserId());
                case "3" -> deleteClassFlow(sc, classService, user.getUserId());
                case "4" -> {
                    List<WorkoutClass> mine = classService.byTrainer(user.getUserId());
                    if (mine.isEmpty()) System.out.println("(no classes yet)");
                    else mine.forEach(System.out::println);
                }
                case "5" -> purchaseMembershipFlow(sc, membershipService, user);
                case "6" -> {
                    var items = merchService.listAll();
                    if (items.isEmpty()) System.out.println("(no merch yet)");
                    else items.forEach(System.out::println);
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void memberMenu(Scanner sc, User user) {
        MembershipService membershipService = new MembershipService();
        WorkoutClassService classService = new WorkoutClassService();
        GymMerchService merchService = new GymMerchService();

        while (true) {
            System.out.println("\n=== Member Menu ===");
            System.out.println("1) Browse workout classes");
            System.out.println("2) View my membership expenses");
            System.out.println("3) Purchase membership");
            System.out.println("4) View merch list");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    List<WorkoutClass> list = classService.upcoming();
                    if (list.isEmpty()) System.out.println("(no upcoming classes yet)");
                    else list.forEach(System.out::println);
                }
                case "2" -> {
                    var list = membershipService.getForMember(user.getUserId());
                    double total = 0;
                    if (list.isEmpty()) System.out.println("You have no memberships yet.");
                    else {
                        System.out.println("-- My memberships --");
                        for (Membership m : list) {
                            System.out.println(m);
                            total += m.getMembershipCost();
                        }
                    }
                    System.out.printf("My total membership expenses: $%.2f%n", total);
                }
                case "3" -> purchaseMembershipFlow(sc, membershipService, user);
                case "4" -> {
                    var items = merchService.listAll();
                    if (items.isEmpty()) System.out.println("(no merch yet)");
                    else items.forEach(System.out::println);
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void purchaseMembershipFlow(Scanner sc, MembershipService membershipService, User user) {
        try {
            System.out.println("\n-- Purchase membership --");
            System.out.print("Type (e.g., Monthly/Annual/PT-Pack): ");
            String type = sc.nextLine().trim();
            System.out.print("Description: ");
            String desc = sc.nextLine().trim();
            System.out.print("Cost (e.g., 49.99): ");
            double cost = Double.parseDouble(sc.nextLine().trim());
            var saved = membershipService.purchase(user.getUserId(), type, desc, cost);
            System.out.println("Purchased: " + saved);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid cost. Please enter a number like 49.99");
        } catch (Exception e) {
            System.out.println("Purchase failed: " + e.getMessage());
        }
    }

    private static void createClassFlow(Scanner sc, WorkoutClassService svc, int trainerId) {
        try {
            System.out.println("\n-- Create class --");
            System.out.print("Type (e.g., Yoga/Spin/HIIT): ");
            String type = sc.nextLine().trim();
            System.out.print("Description: ");
            String desc = sc.nextLine().trim();
            System.out.print("When (yyyy-MM-dd HH:mm) or leave blank: ");
            String whenStr = sc.nextLine().trim();
            var when = parseWhen(whenStr);
            var saved = svc.create(trainerId, type, desc, when);
            System.out.println("Created: " + saved);
        } catch (Exception e) {
            System.out.println("Create failed: " + e.getMessage());
        }
    }

    private static void updateClassFlow(Scanner sc, WorkoutClassService svc, int trainerId) {
        try {
            System.out.println("\n-- Update class --");
            System.out.print("Class ID to update: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("New type: ");
            String type = sc.nextLine().trim();
            System.out.print("New description: ");
            String desc = sc.nextLine().trim();
            System.out.print("New when (yyyy-MM-dd HH:mm) or blank for NULL: ");
            String whenStr = sc.nextLine().trim();
            var when = parseWhen(whenStr);

            boolean ok = svc.update(id, trainerId, type, desc, when);
            System.out.println(ok ? "Updated." : "No class updated (check ID or ownership).");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid class id.");
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private static void deleteClassFlow(Scanner sc, WorkoutClassService svc, int trainerId) {
        try {
            System.out.println("\n-- Delete class --");
            System.out.print("Class ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            boolean ok = svc.delete(id, trainerId);
            System.out.println(ok ? "Deleted." : "No class deleted (check ID or ownership).");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid class id.");
        } catch (Exception e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    private static OffsetDateTime parseWhen(String input) {
        if (input == null || input.isBlank()) return null;
        LocalDateTime ldt = LocalDateTime.parse(input, INPUT_FMT);
        ZoneId zone = ZoneId.systemDefault();
        return ldt.atZone(zone).toOffsetDateTime();
    }

    private static String nullSafe(String s) { return (s == null ? "" : s); }
}
