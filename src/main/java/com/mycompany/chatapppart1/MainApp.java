package com.mycompany.chatapppart1;  // Package declaration

import java.util.Scanner;  // Import Scanner for user input

/**
 * Entry point for the ChatApp application.
 * Handles user registration, login, and the main QuickChat menu.
 *
 * Part 3 changes:
 *   - main() now calls runRegistration() and runLogin() instead of duplicating that logic.
 *   - Calls Message.loadStoredMessages() right after login (before the menu).
 *   - Adds menu option 4 "Stored Messages".
 *   - Adds storedMessagesMenu() with six sub-options (a-f).
 *
 * @author Student
 * @version 3.0 (Part 3 - Stored Messages Menu)
 */
public class MainApp {

    /**
     * Application entry point.
     * Delegates registration and login to helper methods to avoid code duplication,
     * then runs the main QuickChat menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);  // Create Scanner object
        Login login = new Login();  // Create Login instance

        // Delegate to helper methods - avoids duplicating registration and login logic
        String registerMessage = runRegistration(input, login);  // Run registration flow

        if (registerMessage.equals("User registered successfully.")) {
            runLogin(input, login);  // Run login flow

            // Part 3: load any messages stored in previous sessions BEFORE showing the menu
            Message.loadStoredMessages();  // Load saved messages from file

            // ── Main Menu ─────────────────────────────────────────────────────
            System.out.println("\nWelcome to QuickChat.");
            System.out.print("How many messages do you want to send? ");
            int numMessages = Integer.parseInt(input.nextLine());  // Get message count

            boolean running = true;  // Menu loop flag
            while (running) {
                System.out.println("\n--- Menu ---");
                System.out.println("1) Send Messages");
                System.out.println("2) Show Recently Sent Messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                System.out.print("Choose an option: ");
                String choice = input.nextLine();  // Get user choice

                switch (choice) {
                    case "1":
                        sendMessages(input, numMessages);  // Send messages
                        break;
                    case "2":
                        System.out.println("\n--- All Sent Messages ---");
                        System.out.println(Message.printMessages());  // Display messages
                        System.out.println("Total messages sent: " + Message.returnTotalMessages());  // Show count
                        break;
                    case "3":
                        running = false;  // Exit loop
                        System.out.println("Goodbye!");
                        break;
                    case "4":
                        storedMessagesMenu(input);  // Show stored messages submenu
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

        } else {
            System.out.println("Registration failed. Please restart and try again.");
        }

        input.close();  // Close scanner
    }

    // ── Registration ─────────────────────────────────────────────────────────────

    /**
     * Runs the full registration flow and returns the registration result message.
     * Used by main() to avoid duplicating registration logic.
     *
     * @param input the active Scanner for reading user input
     * @param login the Login instance to use for validation
     * @return the registration result message from Login.registerUser()
     */
    public static String runRegistration(Scanner input, Login login) {
        System.out.println("============ USER REGISTRATION ===============");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();  // Get first name
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();  // Get last name

        // Keep prompting until a valid username is entered
        String username = "", usernameMessage = "";
        while (!usernameMessage.equals("Username successfully captured.")) {
            System.out.print("Enter a username (must contain '_' and be <= 5 characters): ");
            username = input.nextLine();  // Get username
            usernameMessage = login.validateUsername(username);  // Validate it
            System.out.println(usernameMessage);  // Show result
        }

        // Keep prompting until a valid password is entered
        String password = "", passwordMessage = "";
        while (!passwordMessage.equals("Password successfully captured.")) {
            System.out.print("Enter a password (8+ chars, 1 capital, 1 number, 1 special char): ");
            password = input.nextLine();  // Get password
            passwordMessage = login.validatePassword(password);  // Validate it
            System.out.println(passwordMessage);  // Show result
        }

        // Keep prompting until a valid South African phone number is entered
        String phoneNumber = "", phoneMessage = "";
        while (!phoneMessage.equals("Cell phone number successfully added.")) {
            System.out.print("Enter your South African phone number (+27...): ");
            phoneNumber = input.nextLine();  // Get phone number
            phoneMessage = login.validatePhoneNumber(phoneNumber);  // Validate it
            System.out.println(phoneMessage);  // Show result
        }

        String registerMessage = login.registerUser(username, password, phoneNumber, firstName, lastName);  // Register user
        System.out.println("\n" + registerMessage);  // Show registration result
        return registerMessage;  // Return result
    }

    // ── Login ─────────────────────────────────────────────────────────────────────

    /**
     * Runs the login flow until the user logs in successfully.
     * Used by main() to avoid duplicating login logic.
     *
     * @param input the active Scanner for reading user input
     * @param login the Login instance to use for credential checking
     */
    public static void runLogin(Scanner input, Login login) {
        System.out.println("\n=== LOGIN ===");
        boolean loggedIn = false;  // Login flag
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String loginUsername = input.nextLine();  // Get username
            System.out.print("Enter password: ");
            String loginPassword = input.nextLine();  // Get password
            loggedIn = login.loginUser(loginUsername, loginPassword);  // Attempt login
            System.out.println(login.returnLoginStatus(loggedIn));  // Show result
        }
    }

    // ── Send Messages ─────────────────────────────────────────────────────────────

    /**
     * Prompts the user to compose and action the specified number of messages.
     * For each message: validates recipient, takes message body, then lets the
     * user choose Send (1), Disregard (2), or Store (3).
     *
     * @param input       the active Scanner for reading user input
     * @param numMessages the number of messages to process in this batch
     */
    public static void sendMessages(Scanner input, int numMessages) {
        for (int i = 1; i <= numMessages; i++) {  // Loop through each message
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            String recipient;
            Message msg = null;

            // Keep prompting until a valid recipient number is entered
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27...): ");
                recipient = input.nextLine();  // Get recipient number
                Message temp = new Message(i, recipient, "placeholder");  // Create temp message
                String check = temp.checkRecipientCell();  // Validate recipient
                System.out.println(check);  // Show result
                if (check.equals("Cell phone number successfully captured.")) break;  // Exit loop if valid
            }

            // Keep prompting until a valid message body is entered
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                String body = input.nextLine();  // Get message body
                msg = new Message(i, recipient, body);  // Create message object
                String validation = msg.validateMessage();  // Validate message
                System.out.println(validation);  // Show result
                if (validation.equals("Message ready to send.")) break;  // Exit loop if valid
            }

            System.out.println("Message ID:   " + msg.getMessageID());  // Display message ID
            System.out.println("Message Hash: " + msg.getMessageHash());  // Display message hash

            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message");
            System.out.print("Choose: ");
            int action = Integer.parseInt(input.nextLine());  // Get user action
            System.out.println(msg.sentMessage(action));  // Process message based on action
        }

        System.out.println("\n=== All Sent Messages ===");
        System.out.println(Message.printMessages());  // Display all messages
        System.out.println("Total messages sent: " + Message.returnTotalMessages());  // Show total count
    }

    // ── Stored Messages Sub-Menu ──────────────────────────────────────────────────

    /**
     * Displays the Stored Messages sub-menu (option 4) and handles user choices.
     * Each option delegates to a method in Message.java to keep this method clean.
     *
     * Sub-options:
     *   a) Display all stored messages
     *   b) Display the longest stored message
     *   c) Search by message ID
     *   d) Search by recipient
     *   e) Delete by message hash
     *   f) Display full report
     *   g) Return to main menu
     *
     * @param input the active Scanner for reading user input
     */
    public static void storedMessagesMenu(Scanner input) {
        boolean inSubMenu = true;  // Submenu loop flag

        while (inSubMenu) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("a) Display all stored messages");
            System.out.println("b) Display longest message");
            System.out.println("c) Search by message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by message hash");
            System.out.println("f) Display full report");
            System.out.println("g) Return to main menu");
            System.out.print("Choose an option: ");
            String subChoice = input.nextLine().trim().toLowerCase();  // Get submenu choice

            switch (subChoice) {
                case "a":
                    System.out.println("\n--- All Stored Messages ---");
                    System.out.println(Message.printMessages());  // Show all messages
                    break;
                case "b":
                    System.out.println("\n--- Longest Message ---");
                    System.out.println(Message.displayLongestMessage());  // Show longest message
                    break;
                case "c":
                    System.out.print("Enter message ID to search: ");
                    String searchID = input.nextLine().trim();  // Get ID to search
                    System.out.println(Message.searchByMessageID(searchID));  // Search by ID
                    break;
                case "d":
                    System.out.print("Enter recipient number to search: ");
                    String searchRecipient = input.nextLine().trim();  // Get recipient to search
                    System.out.println(Message.searchByRecipient(searchRecipient));  // Search by recipient
                    break;
                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String deleteHash = input.nextLine().trim();  // Get hash to delete
                    System.out.println(Message.deleteByHash(deleteHash));  // Delete by hash
                    break;
                case "f":
                    System.out.println("\n--- Full Message Report ---");
                    System.out.println(Message.printMessages());  // Display full report
                    break;
                case "g":
                    inSubMenu = false;  // Return to main menu
                    break;
                default:
                    System.out.println("Invalid option. Please choose a to g.");
            }
        }
    }
}