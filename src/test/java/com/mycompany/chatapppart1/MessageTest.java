/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppart1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Message class.
 * Preserves all Part 2 tests unchanged and adds six new Part 3 tests.
 *
 * POE Test Data (exact flags from the rubric PDF):
 *   Message 1 - "Did you get the cake?"                                    +27834557896  -> SENT
 *   Message 2 - "Where are you? You are late! I have asked you to be on time." +27838884567 -> STORED
 *   Message 3 - "Yohoooo, I am at your gate."                              +27834484567  -> DISREGARD
 *   Message 4 - "It is dinner time !"  (developer number 0838884567)                     -> SENT
 *   Message 5 - "Ok, I am leaving without you."                            +27838884567  -> STORED
 *
 * @author Student
 * @version 3.0 (Part 3 - Arrays, Search, Delete, Report)
 */
public class MessageTest {

    // ── Part 2 test messages (kept exactly as before) ─────────────────────────
    private Message message1;
    private Message message2;

    // ── POE Part 3 exact test data ────────────────────────────────────────────
    private static final String MSG1 = "Did you get the cake?";
    private static final String MSG2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String MSG3 = "Yohoooo, I am at your gate.";
    private static final String MSG4 = "It is dinner time !";
    private static final String MSG5 = "Ok, I am leaving without you.";

    private static final String REC1 = "+27834557896";
    private static final String REC2 = "+27838884567";
    private static final String REC3 = "+27834484567";
    private static final String REC4 = "0838884567";   // Developer number - no international code
    private static final String REC5 = "+27838884567";

    /**
     * Runs before every test.
     * Re-creates the Part 2 test messages and resets all static arrays so
     * tests cannot affect each other.
     */
    @BeforeEach
    public void setUp() {
        // Part 2 messages
        message1 = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message(2, "+27575975889", "Hi Keegan, did you receive the payment?");

        // Reset all static arrays before every test so they are independent
        Message.clearAllArrays();
    }

    // ═════════════════════════════════════════════════════════════════════════════
    // PART 2 TESTS (unchanged)
    // ═════════════════════════════════════════════════════════════════════════════

    // Test 1 - Message length success
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1.validateMessage());
    }

    // Test 2 - Message length failure
   @Test
public void testMessageLengthFailure() {
    // 260 characters exceeds the 250 limit
    Message longMsg = new Message(1, "+27718693002", "A".repeat(260));
    assertEquals("Message exceeds 250 characters by 10; please reduce the size.", longMsg.validateMessage());
}

    // Test 3 - Recipient success
    @Test
    public void testRecipientSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    // Test 4 - Recipient failure - number missing the '+' international code
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
        // Expected: hash ends with first word "HI" + last word "TONIGHT"
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
        // Check that every hash is valid, non-empty, uppercase, and contains colons
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

    // Test 9 - Message ID is not longer than 10 characters
    @Test
    public void testMessageIDLength() {
        assertTrue(message1.checkMessageID(),
            "Message ID should be 10 characters or fewer");
    }

    // ═════════════════════════════════════════════════════════════════════════════
    // PART 3 TESTS - Six new tests using exact POE test data
    // ═════════════════════════════════════════════════════════════════════════════

    /**
     * TEST 1 (Part 3) - Sent messages array is correctly populated.
     *
     * Messages 1 and 4 are flagged Sent in the POE.
     * Message 3 is flagged Disregard (must NOT appear in sent array).
     * Messages 2 and 5 are flagged Stored.
     *
     * The system returns: "Did you get the cake?", "It is dinner time!"
     */
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        // Process all five messages with their correct POE flags
        new Message(1, REC1, MSG1).sentMessage(1); // Sent
        new Message(2, REC2, MSG2).sentMessage(3); // Stored
        new Message(3, REC3, MSG3).sentMessage(2); // Disregard
        new Message(4, REC4, MSG4).sentMessage(1); // Sent
        new Message(5, REC5, MSG5).sentMessage(3); // Stored

        String report = Message.printMessages();

        assertTrue(report.contains(MSG1),
            "Report should contain message 1: " + MSG1);
        assertTrue(report.contains(MSG4),
            "Report should contain message 4: " + MSG4);
        assertFalse(report.contains(MSG3),
            "Disregarded message 3 should NOT appear in the report");
    }

    /**
     * TEST 2 (Part 3) - Display the longest message.
     *
     * Test data: messages 1-4 loaded into storedMessages.
     * The system returns:
     * "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    public void testDisplayLongestMessageReturnsCorrectMessage() {
        // Populate storedMessages with messages 1 to 4 using the test helper
        Message.addToStoredMessages(MSG1);
        Message.addToStoredMessages(MSG2);
        Message.addToStoredMessages(MSG3);
        Message.addToStoredMessages(MSG4);

        String longest = Message.displayLongestMessage();

        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            longest
        );
    }

    /**
     * TEST 3 (Part 3) - Search for a message ID and return the correct message.
     *
     * Test data: message 4, developer number 0838884567.
     * The system returns: "It is dinner time!"
     */
    @Test
    public void testSearchByMessageIDReturnsCorrectMessage() {
        // Process message 4 as Sent so its ID is registered in the parallel arrays
        Message msg4 = new Message(4, REC4, MSG4);
        msg4.sentMessage(1);

        // Search using the ID that was actually generated for this message
        String result = Message.searchByMessageID(msg4.getMessageID());

        assertEquals("It is dinner time !", result);
    }

    /**
     * TEST 4 (Part 3) - Search all messages for a particular recipient.
     *
     * Test data: +27838884567 (used by messages 2 and 5).
     * The system returns both:
     *   "Where are you? You are late! I have asked you to be on time."
     *   "Ok, I am leaving without you."
     */
    @Test
    public void testSearchByRecipientReturnsAllMatchingMessages() {
        // Process messages 2 and 5 so they appear in the sent arrays
        new Message(2, REC2, MSG2).sentMessage(3); // Stored (also goes into sent arrays)
        new Message(5, REC5, MSG5).sentMessage(3); // Stored

        String result = Message.searchByRecipient(REC2);

        assertTrue(result.contains(MSG2),
            "Result should contain message 2");
        assertTrue(result.contains(MSG5),
            "Result should contain message 5");
    }

    /**
     * TEST 5 (Part 3) - Delete a message using a message hash.
     *
     * Test data: Test Message 2 (flagged Stored in POE).
     * The system returns:
     * "Message: Where are you? You are late! I have asked you to be on time. successfully deleted."
     */
    @Test
    public void testDeleteByHashRemovesCorrectMessage() {
        // Process message 2 so it is registered in the parallel arrays
        Message msg2 = new Message(2, REC2, MSG2);
        msg2.sentMessage(3); // Stored

        String result = Message.deleteByHash(msg2.getMessageHash());

        assertEquals(
            "Message: " + MSG2 + " successfully deleted.",
            result
        );
    }

    /**
     * TEST 6 (Part 3) - Display report shows all required fields.
     *
     * The system returns a report that shows all sent messages including:
     * Message Hash, Recipient, and Message text.
     */
    @Test
    public void testDisplayReportContainsRequiredFields() {
        // Process messages 1 and 4 as Sent (the two Sent messages in the POE)
        new Message(1, REC1, MSG1).sentMessage(1);
        new Message(4, REC4, MSG4).sentMessage(1);

        String report = Message.printMessages();

        assertTrue(report.contains("Message Hash:"),
            "Report should contain 'Message Hash:' label");
        assertTrue(report.contains("Recipient:"),
            "Report should contain 'Recipient:' label");
        assertTrue(report.contains(MSG1),
            "Report should contain message 1 text");
        assertTrue(report.contains(REC1),
            "Report should contain recipient 1");
        assertTrue(report.contains(MSG4),
            "Report should contain message 4 text");
    }
}