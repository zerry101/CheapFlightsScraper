package org.example;

import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;

public class SQT {
    // TreeMap to act as our self-balancing search tree
    private final TreeMap<String, Integer> search_Freq_Map;

    public SQT() {
        search_Freq_Map = new TreeMap<>();
    }

    // Function to handle search queries
    public void handle_query(String quueery) {
        search_Freq_Map.put(quueery, search_Freq_Map.getOrDefault(quueery, 0) + 1);
    }

    // Function to display top N searches
    public void d_Top_Searches(int topN) {
        search_Freq_Map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) //sorting the map to bring the words with more searches  to the top
                .limit(topN) // limiting the results to the no. wanted by user
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue())); //fetching and displaying the stored count of each search query.
    }

    // Main function to simulate the tracking
    public static void main(String[] args) {
        SQT traackr = new SQT();
        Scanner s = new Scanner(System.in);

        // Simulating search queries
        while (true) {
            System.out.println("Write search quueery (or type 'exit' to end  program): ");
            String quueery = s.nextLine();
            if (quueery.equalsIgnoreCase("Exit")) {
                break;
            }
            traackr.handle_query(quueery);
        }

        // Display top searches
        System.out.println("Tell the no. of top seaarches to show: ");
        int topN = s.nextInt();
        traackr.d_Top_Searches(topN);
    }
}
