package Test1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Map.Entry;

public class SearchFrequencyTracker {
//    private static Map<String, Integer> searchFrequency = new HashMap<>();

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String input;
//
//        System.out.println("Enter your search queries (type 'exit' to quit and display top searches):");
//
//        while (true) {
//            System.out.print("Search Query: ");
//            input = scanner.nextLine();
//
//            if (input.equalsIgnoreCase("exit")) {
//                break;
//            }
//
//            processSearchQuery(input, searchFrequency);
//        }
//
//        // Display the top searches and generate CSV file
//        displayTopSearches(searchFrequency, 10); // Display top 10 searches
//        scanner.close();
    }

    public static void processSearchQuery(String query, Map<String, Integer> searchFrequency) {
        String[] words = query.split("\\s+"); // Split the query into words
        for (String word : words) {
            word = word.toLowerCase(); // Convert to lower case for case-insensitive matching
            searchFrequency.put(word, searchFrequency.getOrDefault(word, 0) + 1); // Update frequency
        }
    }

    public static void displayTopSearches(Map<String, Integer> searchFrequency, int topN) {
        PriorityQueue<Entry<String, Integer>> topSearches = new PriorityQueue<>(
                (a, b) -> b.getValue().compareTo(a.getValue()));

        topSearches.addAll(searchFrequency.entrySet());

        System.out.println("Top " + topN + " Searches:");
        try (FileWriter csvWriter = new FileWriter("top_searches.csv")) {
            csvWriter.append("Rank,Search Term,Frequency\n");
            for (int i = 0; i < topN && !topSearches.isEmpty(); i++) {
                Entry<String, Integer> entry = topSearches.poll();
                System.out.println((i + 1) + ". " + entry.getKey() + ": " + entry.getValue() + " searches");

                // Write to CSV file
                csvWriter.append(String.valueOf(i + 1))
                        .append(",")
                        .append(entry.getKey())
                        .append(",")
                        .append(String.valueOf(entry.getValue()))
                        .append("\n");
            }
            System.out.println("CSV file 'top_searches.csv' has been generated.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
