package com.mycompany.chatapppart1;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

public class Message {
    
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private static int totalMessagesSent = 0;
    private static List<Message> sentMessages = new ArrayList<>();

    public Message(int messageNumber, String recipient, String message) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    public String createMessageHash() {
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String firstTwo = messageID.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient.startsWith("+") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String validateMessage() {
        if (message.length() <= 250) {
            return "Message ready to send.";
        }
        return "Please enter a message of less than 250 characters.";
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID:   ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient:    ").append(m.recipient).append("\n");
            sb.append("Message:      ").append(m.message).append("\n");
            sb.append("----------------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        try (FileWriter writer = new FileWriter("messages.json", true)) {
            writer.write("{\n");
            writer.write("  \"messageID\": \"" + messageID + "\",\n");
            writer.write("  \"messageNumber\": " + messageNumber + ",\n");
            writer.write("  \"recipient\": \"" + recipient + "\",\n");
            writer.write("  \"message\": \"" + message + "\",\n");
            writer.write("  \"messageHash\": \"" + messageHash + "\"\n");
            writer.write("}\n");
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    public String getMessageID()     { return messageID; }
    public String getRecipient()     { return recipient; }
    public String getMessage()       { return message; }
    public String getMessageHash()   { return messageHash; }
    public int    getMessageNumber() { return messageNumber; }
}