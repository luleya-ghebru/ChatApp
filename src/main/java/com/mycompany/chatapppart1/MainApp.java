package com.mycompany.chatapppart1;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();
        
        // Display a header to indicate the start of the registration process
        System.out.println("============ USER REGISTRATION ===============");
        // Prompt the user to enter their first name and store the input
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        // Prompt the user to enter their last name and store the input
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();
        
        // --- USERNAME VALIDATION LOOP ---
        String username = "";
        String usernameMessage = "";
        while (!usernameMessage.equals("Username successfully captured.")) {
            System.out.print("Enter a username (must contain '_' and be <= 5 characters): ");
            username = input.nextLine();
            usernameMessage = login.validateUsername(username);
            System.out.println(usernameMessage);
        } // this is a new change that we just did 
        
        // Password loop
        String password = "";
        String passwordMessage = "";
        while (!passwordMessage.equals("Password successfully captured.")) {
            System.out.print("Enter a password (8+ chars, 1 capital, 1 number, 1 special char): ");
            password = input.nextLine();
            passwordMessage = login.validatePassword(password);
            System.out.println(passwordMessage);
        }
        
        // Phone number loop
        String phoneNumber = "";
        String phoneMessage = "";
        while (!phoneMessage.equals("Cell phone number successfully added.")) {
            System.out.print("Enter your South African phone number (+27...): ");
            phoneNumber = input.nextLine();
            phoneMessage = login.validatePhoneNumber(phoneNumber);
            System.out.println(phoneMessage);
        }
        
        // Register and display result
        String registerMessage = login.registerUser(username, password, phoneNumber, firstName, lastName);
        System.out.println("\n" + registerMessage);
        
        // Login section
        if (registerMessage.equals("User registered successfully.")) {
            System.out.println("\n=== LOGIN ===");
            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.print("Enter username: ");
                String loginUsername = input.nextLine();
                System.out.print("Enter password: ");
                String loginPassword = input.nextLine();
                loggedIn = login.loginUser(loginUsername, loginPassword);
                System.out.println(login.returnLoginStatus(loggedIn));
            }
            
            // --- PART 2: QuickChat Menu ---
            System.out.println("\nWelcome to QuickChat.");
            
            System.out.print("How many messages do you want to send? ");
            int numMessages = Integer.parseInt(input.nextLine());
            
            boolean running = true;
            while (running) {
                System.out.println("\n--- Menu ---");
                System.out.println("1) Send Messages");
                System.out.println("2) Show Recently Sent Messages");
                System.out.println("3) Quit");
                System.out.print("Choose an option: ");
                String choice = input.nextLine();
                
                switch (choice) {
                    case "1":
                        sendMessages(input, numMessages);
                        break;
                    case "2":
                        System.out.println("Coming Soon.");
                        break;
                    case "3":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            
        } else {
            System.out.println("Registration failed. Please restart and try again.");
        }
        
        input.close();
    }
    
    public static void sendMessages(Scanner input, int numMessages) {
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            String recipient;
            Message msg = null;
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27...): ");
                recipient = input.nextLine();
                Message temp = new Message(i, recipient, "placeholder");
                String check = temp.checkRecipientCell();
                System.out.println(check);
                if (check.equals("Cell phone number successfully captured.")) break;
            }

            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                String body = input.nextLine();
                msg = new Message(i, recipient, body);
                String validation = msg.validateMessage();
                System.out.println(validation);
                if (validation.equals("Message ready to send.")) break;
            }

            System.out.println("Message ID:   " + msg.getMessageID());
            System.out.println("Message Hash: " + msg.getMessageHash());

            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message");
            System.out.print("Choose: ");
            int action = Integer.parseInt(input.nextLine());
            System.out.println(msg.sentMessage(action));
        }

        System.out.println("\n=== All Sent Messages ===");
        System.out.println(Message.printMessages());
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }
    
    public static String runRegistration(Scanner input, Login login) {
        System.out.println("============ USER REGISTRATION ===============");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        String username = "", usernameMessage = "";
        while (!usernameMessage.equals("Username successfully captured.")) {
            System.out.print("Enter a username (must contain '_' and be <= 5 characters): ");
            username = input.nextLine();
            usernameMessage = login.validateUsername(username);
            System.out.println(usernameMessage);
        }

        String password = "", passwordMessage = "";
        while (!passwordMessage.equals("Password successfully captured.")) {
            System.out.print("Enter a password (8+ chars, 1 capital, 1 number, 1 special char): ");
            password = input.nextLine();
            passwordMessage = login.validatePassword(password);
            System.out.println(passwordMessage);
        }

        String phoneNumber = "", phoneMessage = "";
        while (!phoneMessage.equals("Cell phone number successfully added.")) {
            System.out.print("Enter your South African phone number (+27...): ");
            phoneNumber = input.nextLine();
            phoneMessage = login.validatePhoneNumber(phoneNumber);
            System.out.println(phoneMessage);
        }

        String registerMessage = login.registerUser(username, password, phoneNumber, firstName, lastName);
        System.out.println("\n" + registerMessage);
        return registerMessage;
    }

    public static void runLogin(Scanner input, Login login) {
        System.out.println("\n=== LOGIN ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String loginUsername = input.nextLine();
            System.out.print("Enter password: ");
            String loginPassword = input.nextLine();
            loggedIn = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loggedIn));
        }
    }
}