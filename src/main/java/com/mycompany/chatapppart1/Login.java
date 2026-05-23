/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppart1;


/**
 *
 * @author Student
 */
public class Login {
    private String username;
    private String password;
    
    private String phoneNumber;
    private String firstName;
    private String lastName;
    
    public Login(String username, String password, String phoneNumber, String firstName, String lastName) {
    this.username = username;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.firstName = firstName;
    this.lastName = lastName;
}
     // Default constructor
    public Login() {

    }
    
      // Returns true if username contains '_' and is <= 5 characters
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    
    // Returns true if password is >= 8 chars and has a capital, digit, and special character
    public boolean checkPasswordComplexity(String password) {
        boolean hasSpecial = false;
        boolean hasNumber = false;
        boolean hasCapital = false;
        
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }
    
    // Returns true if phone starts with +27 and is <= 12 characters
    public boolean checkCellPhoneNumber() {
        return this.phoneNumber.startsWith("+27") && this.phoneNumber.length() <= 12;
    }
    
    // Validates username format, returns success or error message
    public String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Error: Username cannot be empty.";
        }
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        return "Username successfully captured.";
    }
    
    // Validates password complexity, returns success or error message
    public String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Error: Password cannot be empty.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        return "Password successfully captured.";
    }
    
    // Validates phone number format, returns success or error message
    public String validatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "Error: Phone number cannot be empty.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "Cell phone number successfully added.";
    }
    
    // Register user - only stores data if all fields are valid
    public String registerUser(String username, String password, String phone, String fName, String lName) {
        boolean isUsernameValid = checkUserName(username);
        boolean isPasswordValid = checkPasswordComplexity(password);
        this.phoneNumber = phone;
        boolean isPhoneValid = checkCellPhoneNumber();
        
        // Return early on first invalid field
        if (!isUsernameValid) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!isPasswordValid) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!isPhoneValid) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        // Only store if everything is valid
        this.username = username;
        this.password = password;
        this.phoneNumber = phone;
        this.firstName = fName;
        this.lastName = lName;
        
        return "User registered successfully.";
    }
    
     // Returns true if the provided credentials match the stored ones
    public boolean loginUser(String username, String password) {
        return this.username != null && this.username.equals(username) &&
               this.password != null && this.password.equals(password);
    }
    
   // Returns a welcome message on success, or an error message on failure
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}



