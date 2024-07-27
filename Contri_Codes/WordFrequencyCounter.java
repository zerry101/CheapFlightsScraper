package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordFrequencyCounter {
    public static void main(String[] args) {
        try {
            // Specify the path to the CSV file
            // This path points to the exact location of the CSV file on your system
            Path filePath = Paths.get("C:/Users/helir/Downloads/trial_flightdata_Ass2.csv");

            // Read all lines from the CSV file into a list of strings
            // Files.readAllLines() reads all lines from a file and returns a list of strings,
            // where each string represents one line in the file
            List<String> fileLines = Files.readAllLines(filePath);

            // Create a map to store word frequencies
            // HashMap is used here to store key-value pairs, where the key is the word and the value is its frequency
            Map<String, Integer> wordFrequencyMap = new HashMap<>();

            // Iterate through each line in the list of lines from the file
            for (String line : fileLines) {
                // Split the line into words based on commas
                // String.split(",") splits the string into an array of words using comma as the delimiter
                String[] words = line.split(",");

                // Process each word in the array of words
                for (String word : words) {
                    // Remove leading and trailing whitespace from the word
                    // String.trim() removes any leading and trailing whitespace from the word
                    word = word.trim();

                    // Check if the word is not empty after trimming whitespace
                    if (!word.isEmpty()) {
                        // Convert the word to lowercase to ensure case-insensitive counting
                        // This means "Word" and "word" will be treated as the same word
                        word = word.toLowerCase();

                        // Update the word frequency in the map
                        // If the word is already present in the map, increment its count by 1
                        // If the word is not present, add it to the map with a count of 1
                        wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                    }
                }
            }

            // Convert the word frequency map to a list of map entries
            // Map.entrySet() returns a set view of the mappings contained in the map
            // new ArrayList<>(...) creates an ArrayList from this set
            List<Map.Entry<String, Integer>> wordFrequencyList = new ArrayList<>(wordFrequencyMap.entrySet());

            // Sort the list by frequency in descending order
            // Collections.sort() is used to sort the list, with a custom comparator that compares the values (frequencies) in descending order
            wordFrequencyList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            // Define the number of top frequent words to display
            // Adjust this value to show more or fewer words in the output
            int topN = 12;

            // Print a heading for the output
            // This is just for better readability of the output
            System.out.println("\nTop " + topN + " Frequent Words are:");

            // Display the top N most frequent words
            // Iterate through the sorted list and print the top N entries
            for (int i = 0; i < Math.min(topN, wordFrequencyList.size()); i++) {
                // Get the current entry (word and its frequency) from the sorted list
                Map.Entry<String, Integer> entry = wordFrequencyList.get(i);

                // Print the word and its frequency
                // Map.Entry.getKey() returns the word, and Map.Entry.getValue() returns the frequency
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs
            // This can happen if the file is not found or there's an error reading the file
            e.printStackTrace();
        }
    }
}
