package com.mycompany.chatapppart1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import org.json.JSONObject;

/**
 * Represents a single chat message in the QuickChat application.
 *
 * Handles message validation, hash creation, sending, storing, and discarding.
 * Maintains five static parallel arrays to track all messages processed during
 * the session, and provides search, delete, and report methods for Part 3.
 *
 * @author Student
 * @version 3.0 (Part 3 - Arrays, Search, Delete, Report)
 */
public class Message {

    // ── Instance fields ──────────────────────────────────────────────────────────
    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    // ── Static parallel arrays (shared across all Message objects) ───────────────

    /** Full text of every message the user chose to Send or Store. */
    private static List<String> sentMessages        = new ArrayList<>();

    /** Full text of every message the user chose to Discard. */
    private static List<String> disregardedMessages = new ArrayList<>();

    /**
     * Messages loaded back from messages.json at startup.
     * Populated ONLY by loadStoredMessages() - NOT by the user selecting Store.
     */
    private static List<String> storedMessages      = new ArrayList<>();

    /** Hash string for every Sent or Stored message (parallel to sentMessages). */
    private static List<String> messageHashes       = new ArrayList<>();

    /** Unique ID for every Sent or Stored message (parallel to sentMessages). */
    private static List<String> messageIDs          = new ArrayList<>();

    /**
     * Recipient for every Sent or Stored message (parallel to sentMessages).
     * Required so searchByRecipient() can match messages to a number.
     */
    private static List<String> recipientList       = new ArrayList<>();

    // ── Constructor ──────────────────────────────────────────────────────────────

    /**
     * Creates a new Message and immediately generates its ID and hash.
     *
     * @param messageNumber the sequence number of this message in the session
     * @param recipient     the recipient's cell number (e.g. +27834557896)
     * @param message       the message body text
     */
    public Message(int messageNumber, String recipient, String message) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.message       = message;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    // ── ID and Hash ──────────────────────────────────────────────────────────────

    /**
     * Generates a random 10-digit numeric message ID.
     *
     * @return a 10-character string of digits
     */
    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    /**
     * Creates a hash in the format XX:N:FIRSTWORDLASTWORD (all uppercase).
     *   XX                = first two digits of the message ID
     *   N                 = message number
     *   FIRSTWORDLASTWORD = first and last words of the message body concatenated
     *
     * @return the formatted hash string
     */
    public String createMessageHash() {
        String[] words   = message.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String firstTwo  = messageID.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // ── Validation ───────────────────────────────────────────────────────────────

    /**
     * Checks that the message ID does not exceed 10 characters.
     *
     * @return true if the ID length is valid
     */
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    /**
     * Validates the recipient cell number.
     * Must start with '+27' (South African international code) and be 12 characters or fewer.
     *
     * @return a status message indicating success or failure
     */
    public String checkRecipientCell() {
        if (recipient.startsWith("+27") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
             + "Please correct the number and try again.";
    }

    /**
     * Validates that the message body does not exceed 250 characters.
     * If it does, reports exactly how many characters over the limit it is.
     *
     * @return a status message indicating whether the message is ready to send
     */
    public String validateMessage() {
        if (message.length() <= 250) {
            return "Message ready to send.";
        }
        int overage = message.length() - 250;
        return "Message exceeds 250 characters by " + overage + "; please reduce the size.";
    }

    // ── Send / Store / Discard ───────────────────────────────────────────────────

    /**
     * Processes the user's action choice for this message.
     *
     * Choice 1 - Send:    adds to sentMessages, messageHashes, messageIDs, recipientList.
     * Choice 2 - Discard: adds message text to disregardedMessages only.
     * Choice 3 - Store:   writes to JSON file AND adds to sentMessages,
     *                      messageHashes, messageIDs, and recipientList so that
     *                      stored messages appear in searches and the report.
     *                      The storedMessages array is populated separately by
     *                      loadStoredMessages() when the app starts.
     *
     * @param choice 1 = Send, 2 = Discard, 3 = Store
     * @return a status message describing the outcome
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this.message);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                return "Message successfully sent.";

            case 2:
                disregardedMessages.add(this.message);
                return "Press 0 to delete the message.";

            case 3:
                sentMessages.add(this.message);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                storeMessage();
                return "Message successfully stored.";

            default:
                return "Invalid choice.";
        }
    }

    // ── JSON Write ───────────────────────────────────────────────────────────────

    /**
     * Appends this message to messages.json using the org.json library.
     * Each message is written as a properly formatted JSON object.
     *
     * Attribution: org.json library used to build JSON objects.
     * Source: https://mvnrepository.com/artifact/org.json/json
     */
    public void storeMessage() {
        try (FileWriter writer = new FileWriter("messages.json", true)) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("messageID",     messageID);
            jsonMessage.put("messageNumber", messageNumber);
            jsonMessage.put("recipient",     recipient);
            jsonMessage.put("message",       message);
            jsonMessage.put("messageHash",   messageHash);
            writer.write(jsonMessage.toString(2) + "\n");
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    // ── JSON Read ────────────────────────────────────────────────────────────────

    /**
     * Reads messages.json at startup and loads each stored message body into
     * the storedMessages array using the org.json library to parse each object.
     *
     * Called from MainApp.java immediately after the user logs in successfully,
     * before the menu is displayed. Exits silently if the file does not exist yet.
     *
     * Attribution: org.json library used to parse JSON objects.
     * Source: https://mvnrepository.com/artifact/org.json/json
     */
    public static void loadStoredMessages() {
        File file = new File("messages.json");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder objectBuffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                objectBuffer.append(line);

                // Each JSON object ends with a closing brace
                if (line.trim().equals("}")) {
                    JSONObject jsonObject  = new JSONObject(objectBuffer.toString());
                    String messageValue    = jsonObject.getString("message");
                    if (messageValue != null && !messageValue.isEmpty()) {
                        storedMessages.add(messageValue);
                    }
                    objectBuffer.setLength(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load stored messages: " + e.getMessage());
        }
    }

    // ── Part 3 Feature Methods ───────────────────────────────────────────────────

    /**
     * Searches the storedMessages array and returns the message with the most characters.
     *
     * @return the longest stored message, or a notice if no messages are stored
     */
    public static String displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }
        String longest = "";
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    /**
     * Searches the messageIDs array for a matching ID and returns the corresponding
     * message from sentMessages at the same index (parallel array search).
     *
     * @param id the message ID to search for
     * @return the matching message text, or "Message not found." if no match
     */
    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                return sentMessages.get(i);
            }
        }
        return "Message not found.";
    }

    /**
     * Searches all processed messages and returns every message sent to the
     * given recipient. Multiple matches are all included in the result.
     *
     * @param recipient the cell number to search for (e.g. +27838884567)
     * @return all matching messages as a formatted string, or a not-found notice
     */
    public static String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(recipient)) {
                results.append(sentMessages.get(i)).append("\n");
            }
        }
        if (results.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return results.toString().trim();
    }

    /**
     * Deletes the message matching the given hash from all parallel arrays.
     * Breaks out of the loop immediately after removal to avoid index-shift errors.
     *
     * @param hash the message hash to delete
     * @return a success message with the deleted message text, or "Hash not found."
     */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deletedMessage = sentMessages.get(i);
                messageHashes.remove(i);
                sentMessages.remove(i);
                messageIDs.remove(i);
                recipientList.remove(i);
                return "Message: " + deletedMessage + " successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    /**
     * Builds a formatted report of all sent and stored messages.
     * Each entry shows the Message Hash, Recipient, and Message text,
     * retrieved from the parallel arrays using the same index.
     *
     * @return the full report as a formatted string
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder report = new StringBuilder();
        report.append("=== Message Report ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("Message Hash: ").append(messageHashes.get(i)).append("\n");
            report.append("Recipient:    ").append(recipientList.get(i)).append("\n");
            report.append("Message:      ").append(sentMessages.get(i)).append("\n");
            report.append("----------------------------\n");
        }
        return report.toString();
    }

    /**
     * Returns the total number of messages sent or stored this session.
     *
     * @return the size of the sentMessages array
     */
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // ── Test helpers (package-private) ───────────────────────────────────────────

    /**
     * Clears all static arrays. Called in unit tests to reset state between tests.
     */
    static void clearAllArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
    }

    /**
     * Directly adds a message to the storedMessages array.
     * Used in unit tests to pre-populate without touching the file system.
     *
     * @param msg the message text to add
     */
    static void addToStoredMessages(String msg) {
        storedMessages.add(msg);
    }

    // ── Getters ──────────────────────────────────────────────────────────────────

    /** @return the 10-digit message ID */
    public String getMessageID()     { return messageID; }

    /** @return the recipient cell number */
    public String getRecipient()     { return recipient; }

    /** @return the message body text */
    public String getMessage()       { return message; }

    /** @return the formatted message hash */
    public String getMessageHash()   { return messageHash; }

    /** @return the sequence number of this message */
    public int    getMessageNumber() { return messageNumber; }
}