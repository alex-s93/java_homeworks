package com.alevel.homework23.consoleModule;

import com.alevel.homework23.dao.UserDao;
import com.alevel.homework23.dbHelper.DBConnector;
import com.alevel.homework23.entities.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserExecutor {
    private static Scanner scanner = new Scanner(System.in);

    static void createUser(DBConnector dbConnector) {
        System.out.println("Enter user's email:");
        String email = scanner.nextLine();
        if (UserDao.getCountOfUsersByEmail(dbConnector, email) > 0) {
            throw new RuntimeException("This email already exists in the database");
        }

        System.out.println("Enter user's first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter user's last name:");
        String lastName = scanner.nextLine();

        System.out.println("Enter user's date of birth in the format YYYY-MM-DD:");
        LocalDate birthday = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        System.out.println("Enter user's home address:");
        String address = scanner.nextLine();

        User user = UserDao.buildUser(firstName, lastName, birthday, address, email);
        UserDao.insertUser(dbConnector, user);
    }

    static void updateUser(DBConnector dbConnector) {
        String[] userFields = {"first name", "last name", "date of birth", "address", "email"};

        System.out.println("Enter the email of the user you need to update:");
        String email = scanner.nextLine();
        if (!(UserDao.getCountOfUsersByEmail(dbConnector, email) > 0)) {
            throw new RuntimeException("User with this email is absent in the database");
        }

        System.out.println("What information do you want to update? Please enter one or more parameters from the brackets" +
                " (First name, Last name, Date of birth, Address, Email).");
        System.out.println("Enter the word \"finish\" after entering all the values that you want");

        Map<String, String> newValues = new HashMap<>();
        do {
            System.out.println("Field:");
            String field = scanner.nextLine().toLowerCase();
            if (!Arrays.asList(userFields).contains(field)) {
                System.out.println("The entered name of field does not correspond to any field in the database. Try again!");
                continue;
            }
            System.out.println("New value:");
            String newValue = scanner.nextLine();
            newValues.put(field, newValue);
            System.out.println("Is it all fields what you want to update?");
        } while (!scanner.nextLine().equals("yes"));
        if (newValues.size() == 0) {
            throw new RuntimeException("Nothing to update");
        }
        if (UserDao.updateUser(dbConnector, email, newValues)) {
            System.out.println("User with email '" + email + "' was updated successfully!");
        } else {
            System.out.println("Something went wrong");
        }
    }

    static void deleteUser(DBConnector dbConnector) {
        System.out.println("Enter the email of user which you want to delete:");
        String email = scanner.nextLine();
        if (!(UserDao.getCountOfUsersByEmail(dbConnector, email) > 0)) {
            throw new RuntimeException("User with this email is absent in the database");
        }

        if (UserDao.deleteUser(dbConnector, email)) {
            System.out.println("User with email '" + email + "' was deleted successfully!");
        } else {
            System.out.println("Something went wrong");
        }
    }
}