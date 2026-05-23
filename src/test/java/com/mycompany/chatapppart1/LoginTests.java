/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapppart1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class LoginTests {
    
     // This is testing correctly formatted username
    @Test
    public void testUsernameCorrectlyFormatted() {  // (Farrell, 2018)
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean expected = true;
        boolean actual = l.checkUserName("kyl_1");
        assertEquals(expected, actual);
    }
// Tests that a username with invalid special characters is rejected.
    @Test
    public void testUsernameIncorrectlyFormatted() {  // (Farrell, 2018)
        Login l = new Login("kyl!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean expected = false;
        boolean actual = l.checkUserName("kyl!!!!!!!"); // Fixed to call the method that returns the username validation message
        assertEquals(expected, actual);
    }

   // Tests that a password meeting all complexity rules is accepted.
    @Test
    public void testPasswordCorrectlyFormatted() {  // (Farrell, 2018)
       
        // testing to see if password meets the complexity requirements.
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean expected = true;
        boolean actual = l.checkPasswordComplexity("Ch&&sec@ke99!");
        assertEquals(expected, actual);
    }

    // Tests that a weak password without required characters is rejected.
    @Test
    public void testPasswordIncorrectlyFormatted() {  // (Farrell, 2018)
       
        Login l = new Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        boolean expected = false;
        boolean actual = l.checkPasswordComplexity("password");
        assertEquals(expected, actual);
    }
   // Tests that a phone number in correct international format is accepted.
     @Test
    public void testCellPhoneCorrectlyFormatted() { // checking to see if the cell phone number is correctly formatted.  // (Farrell, 2018)
       
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean expected = true;
        boolean actual = l.checkCellPhoneNumber();
        assertEquals(expected, actual);
       
    }

    // Tests that a phone number missing the international prefix is rejected.
    @Test
    public void testCellPhoneIncorrectlyFormatted() {   // (Farrell, 2018)
       
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith");
        boolean expected = false;
        boolean actual = l.checkCellPhoneNumber();
        assertEquals(expected, actual);
       
    }
 // Tests that login succeeds when correct credentials are provided.
    @Test
    public void testLoginSuccess() { // Checking if the test is true
       
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(l.loginUser("kyl_1", "Ch&&sec@ke99!"));   // (Farrell, 2018)
    }
// Tests that login fails when an incorrect password is provided.
    @Test
    public void testLoginFail() {// Checking if the login fails
       
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertFalse(l.loginUser("kyl_1", "Ch&&sec@ke99"));   // (Farrell, 2018)
    }
    
    // Tests that a correctly formatted username returns true.
    @Test
    public void testUsernameCheckTrue() {   // this is to test username validation correct: true expected
       
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        // Fixes for part 1 improvement
        assertTrue(l.checkUserName("Kyl_1"));   // (Farrell, 2018)
    }
// Tests that an incorrectly formatted username returns false.
    @Test
    public void testUsernameCheckFalse() {
       
        Login l = new Login("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
       
        assertFalse(l.checkUserName("kyle!!!!!!!"));
       
    }

    // Tests that a complex password passes validation and returns true.
    @Test
    public void testPasswordCheckTrue() {
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
       
        assertTrue(l.checkPasswordComplexity("Ch&&sec@ke99!"));
        // Test password complexity validation correct: true expected (Farrell, 2018)
    }
// Tests that a simple password fails validation and returns false.
    @Test
    public void testPasswordCheckFalse() {// (Farrell, 2018)
        Login l = new Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertFalse(l.checkPasswordComplexity("password"));  // Tests password complexity validation correct: false expected
    }
    
    // Tests that a valid international phone number returns true.
    @Test
    public void testCellPhoneCheckTrue() { // this test phone number validation method is correct format: true expected
     
        // (Farrell, 2018)
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(l.checkCellPhoneNumber(), "+27838968976");
    }

    // Tests that a phone number without a country code returns false.
    @Test
    public void testCellPhoneCheckFalse() {
        // this test phone number validation method is correct format: fa;se expected
        Login l = new Login("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith");
        assertFalse(l.checkCellPhoneNumber(), "08966553");
       
    }// (Farrell, 2018)

   
}
/*
References
Farrell, J., 2018. Java Programming. 9th ed. Boston: Cengage Learning.
GeeksforGeeks. (2023). How to match phone numbers in a list to regex pattern in Java? [online] Available at:< https://www.geeksforgeeks.org/how-to-match-phone-numbers-in-a-list-to-regex-pattern-in-java/ > [Accessed 22 Apirl 2025].
W3Schools (2025) How To Create a Password Validation Form. Available at:< https://www.w3schools.com/howto/howto_js_password_validation.asp >(Accessed: 11 April 2025).
*/
    
        
    

