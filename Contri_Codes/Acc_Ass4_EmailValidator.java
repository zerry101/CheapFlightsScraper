

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Acc_Ass4_EmailValidator {

    // Regular expression for validating an Email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static void main(String[] args) {
        // List to store valid emails
        List<String> validEmails = new ArrayList<>();
        // List to store invalid emails
        List<String> invalidEmails = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        printstmnt("Enter email addresses (type 'exit' to stop):");

        while (true) {
            printstmnt("Enter email: ");
            String email = scanner.nextLine();
            // Check if the user wants to exit the input loop
            if (email.equalsIgnoreCase("exit")) {
                break;
            }
            // Validate the email and add it to the appropriate list
            if (isValidEmail(email)) {
                validEmails.add(email);
                printstmnt(email + " is a valid email.");
            } else {
                invalidEmails.add(email);
                printstmnt(email + " is an invalid email.");
            }
        }

        // Save valid emails to a file
        saveEmailsToFile("valid_emails.txt", validEmails);
        // Save invalid emails to a file
        saveEmailsToFile("invalid_emails.txt", invalidEmails);

        // Display all valid emails
        printstmnt("\nAll Valid Emails: " + validEmails);
        // Display all invalid emails
        printstmnt("All Invalid Emails: " + invalidEmails);

        scanner.close(); // Close the scanner to free up resources
    }

    // Method to check if an email is valid
    private static boolean isValidEmail(String email) {
        // Use the regular expression to check if the email matches the pattern
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Method to save emails to a file
    private static void saveEmailsToFile(String filename, List<String> emails) {
        // Try-with-resources statement to automatically close the writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write each email to the file
            for (String email : emails) {
                writer.write(email);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any IO exceptions that occur during file writing
            printstmnt("An error occurred while saving emails: " + e.getMessage());
        }
    }

    // Method to print a string
    private static void printstmnt(String S) {
        System.out.println(S);
    }
}
