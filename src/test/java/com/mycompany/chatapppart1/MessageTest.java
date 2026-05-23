/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppart1;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {
 

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        message1 = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message(2, "+27575975889", "Hi Keegan, did you receive the payment?");
    }

    // Test 1 - Message length success
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1.validateMessage());
    }

    // Test 2 - Message length failure
    @Test
    public void testMessageLengthFailure() {
        Message longMsg = new Message(1, "+27718693002", "A".repeat(260));
        assertEquals("Please enter a message of less than 250 characters.", longMsg.validateMessage());
    }

    // Test 3 - Recipient success
    @Test
    public void testRecipientSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    // Test 4 - Recipient failure
    @Test
    public void testRecipientFailure() {
        Message badRecipient = new Message(1, "08575975889", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", badRecipient.checkRecipientCell());
    }

    // Test 5 - Message hash is uppercase and contains colons
    @Test
    public void testMessageHashFormat() {
        String hash = message1.getMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
        assertEquals(hash, hash.toUpperCase());
    }

    // Test 6 - Message hash ends with correct first and last word
    @Test
    public void testMessageHashContent() {
        String hash = message1.getMessageHash();
        assertTrue(hash.endsWith("HITONIGHT"),
            "Expected hash to end with HITONIGHT but was: " + hash);
    }

    // Test 7 - Message hashes tested in a loop
    @Test
    public void testMessageHashesInLoop() {
        Message[] messages = {
            new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?"),
            new Message(2, "+27575975889", "Hi Keegan, did you receive the payment?")
        };
        for (Message m : messages) {
            String hash = m.getMessageHash();
            assertNotNull(hash);
            assertFalse(hash.isEmpty());
            assertEquals(hash, hash.toUpperCase());
            assertTrue(hash.contains(":"));
        }
    }

    // Test 8 - Message ID is created and not null
    @Test
    public void testMessageIDNotNull() {
        assertNotNull(message1.getMessageID());
        System.out.println("Message ID generated: " + message1.getMessageID());
    }

    // Test 9 - Message ID is not long   
}
