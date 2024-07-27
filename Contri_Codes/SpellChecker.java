import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SpellChecker {

    private final Set<String> vocabulary; // Declaring final set to store vocabulary words - Priyal Thakkar

    // Constructor initializing vocabulary set and loading words from CSV file - Priyal Thakkar
    public SpellChecker(String csvFile) {
        vocabulary = new HashSet<>(); // Initializing vocabulary set as HashSet - Priyal Thakkar
        loadVocabulary(csvFile); // Calling method to load vocabulary from specified CSV file - Priyal Thakkar
    }

    // Method loading vocabulary from CSV file - Priyal Thakkar
    private void loadVocabulary(String csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) { // Creating BufferedReader to read file - Priyal Thakkar
            String line; // Variable storing each line read from file - Priyal Thakkar
            while ((line = br.readLine()) != null) { // Reading lines from file until end reached - Priyal Thakkar
                String[] values = line.split(","); // Splitting each line into words based on commas - Priyal Thakkar
                for (String word : values) { // Iterating through each word in line - Priyal Thakkar
                    vocabulary.add(word.trim().toLowerCase()); // Adding word to vocabulary set after trimming and converting to lowercase - Priyal Thakkar
                }
            }
        } catch (IOException e) { // Catching IOException occurring during file reading - Priyal Thakkar
            e.printStackTrace(); // Printing stack trace of exception for debugging purposes - Priyal Thakkar
        }
    }

    // Method calculating Levenshtein distance between two strings - Priyal Thakkar
    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1]; // Creating 2D array to store distances - Priyal Thakkar

        for (int i = 0; i <= a.length(); i++) { // Looping through each character of first string - Priyal Thakkar
            for (int j = 0; j <= b.length(); j++) { // Looping through each character of second string - Priyal Thakkar
                if (i == 0) { // If first string is empty - Priyal Thakkar
                    dp[i][j] = j; // Distance is length of second string - Priyal Thakkar
                } else if (j == 0) { // If second string is empty - Priyal Thakkar
                    dp[i][j] = i; // Distance is length of first string - Priyal Thakkar
                } else { // If neither string is empty - Priyal Thakkar
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + costOfSubstitution(a.charAt(i - 1), b.charAt(j - 1)), // Cost of substitution - Priyal Thakkar
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)); // Minimum of deletion and insertion costs - Priyal Thakkar
                }
            }
        }

        return dp[a.length()][b.length()]; // Returning calculated distance - Priyal Thakkar
    }

    // Method determining cost of substituting one character for another - Priyal Thakkar
    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1; // Returning 0 if characters are same, otherwise 1 - Priyal Thakkar
    }

    // Method suggesting alternative words from vocabulary based on Levenshtein distance - Priyal Thakkar
    public List<String> suggestAlternatives(String word) {
        List<String> suggestions = new ArrayList<>(); // Creating list to store suggestions - Priyal Thakkar
        for (String vocabWord : vocabulary) { // Iterating through each word in vocabulary - Priyal Thakkar
            if (levenshteinDistance(word, vocabWord) <= 2) { // If distance is within threshold - Priyal Thakkar
                suggestions.add(vocabWord); // Adding word to suggestions list - Priyal Thakkar
            }
        }
        suggestions.sort(Comparator.comparingInt(s -> levenshteinDistance(word, s))); // Sorting suggestions by distance - Priyal Thakkar
        return suggestions; // Returning list of suggestions - Priyal Thakkar
    }

    // Main method executing program
    public static void main(String[] args) {
        String csvFile = "C://Users//priya//IdeaProjects//Priyal_Asg1//Kayak_Data_Asg.csv"; // Path to CSV file - Priyal Thakkar
        SpellChecker spellChecker = new SpellChecker(csvFile); // Creating SpellChecker object and loading vocabulary - Priyal Thakkar

        Scanner scanner = new Scanner(System.in); // Creating Scanner object to read input from user - Priyal Thakkar
        System.out.print("Enter word to check: "); // Prompting user to enter word - Priyal Thakkar
        String wordToCheck = scanner.nextLine(); // Reading user's input - Priyal Thakkar

        List<String> suggestions = spellChecker.suggestAlternatives(wordToCheck); // Getting suggestions for input word - Priyal Thakkar

        if (suggestions.isEmpty()) { // If no suggestions are found - Priyal Thakkar
            System.out.println("No suggestions found for '" + wordToCheck + "'."); // Informing user - Priyal Thakkar
        } else { // If suggestions are found
            System.out.println("Suggestions for '" + wordToCheck + "':"); // Informing user - Priyal Thakkar
            for (String suggestion : suggestions) { // Iterating through suggestions - Priyal Thakkar
                System.out.println(suggestion); // Printing each suggestion - Priyal Thakkar
            }
        }

        scanner.close(); // Closing Scanner object - Priyal Thakkar
    }
}
