import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class dates_asg4 {

    // Method to locate and extract dates in YYYY-MM-DD format //Priyal Thakkar
    public static List<String> findDates(String text) {
        List<String> dates = new ArrayList<>(); // Initialize list to store dates //Priyal Thakkar
        // Define regex to match dates in YYYY-MM-DD format //Priyal Thakkar
        String regex = "\\b(\\d{4})-(\\d{2})-(\\d{2})\\b";
        // Define regex pattern to match dates in format YYYY-MM-DD //Priyal Thakkar
        // \\b - Word boundary, ensures match is at start or end of word //Priyal Thakkar
        // (\\d{4}) - Capturing group for four digits, representing year //Priyal Thakkar
        // - - Literal hyphen, separating year, month, and day //Priyal Thakkar
        // (\\d{2}) - Capturing group for two digits, representing month //Priyal Thakkar
        // - - Literal hyphen, separating month and day //Priyal Thakkar
        // (\\d{2}) - Capturing group for two digits, representing day //Priyal Thakkar
        // \\b - Word boundary, ensures match is at start or end of word //Priyal Thakkar
        Pattern pattern = Pattern.compile(regex); // Compile regex into pattern //Priyal Thakkar
        Matcher matcher = pattern.matcher(text); // Create matcher for input text //Priyal Thakkar

        // Loop through all matches and add to dates list //Priyal Thakkar
        while (matcher.find()) {
            dates.add(matcher.group()); // Add matched date to list //Priyal Thakkar
        }

        return dates; // Return list of found dates //Priyal Thakkar
    }

    // Method to read text content from file //Priyal Thakkar
    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder(); // Initialize StringBuilder for file content //Priyal Thakkar
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line; // Declare variable to hold each line //Priyal Thakkar
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n"); // Append line to content with newline //Priyal Thakkar
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for IO exceptions //Priyal Thakkar
        }
        return content.toString(); // Return file content as string //Priyal Thakkar
    }

    public static void main(String[] args) {
        // Define path to text file //Priyal Thakkar
        String filePath = "Sample_Flight_Data.txt";

        // Read file content into text variable //Priyal Thakkar
        String text = readFile(filePath);

        // Call findDates to extract dates from text //Priyal Thakkar
        List<String> foundDates = findDates(text);

        // Print each found date //Priyal Thakkar
        for (String date : foundDates) {
            System.out.println(date);
        }
    }
}
