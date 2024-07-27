import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Class representing a node in the Trie data structure
class TrieNode {
    // Map to store child nodes, where each key is a character and value is the corresponding TrieNode
    Map<Character, TrieNode> children;
    // List to store names of documents that contain the word ending at this node
    List<String> docNames;

    // Constructor initializes the children map and document names list
    public TrieNode() {
        children = new HashMap<>(); // Initialize the map to store child nodes
        docNames = new ArrayList<>(); // Initialize the list to store document names
    }
}

// Class representing the Trie data structure for inverted indexing
class Trie {
    private TrieNode root; // Root node of the Trie

    // Constructor initializes the root node
    public Trie() {
        root = new TrieNode(); // Create a new root node
    }

    // Method to insert a word into the Trie along with the name of the document containing the word
    public void insert(String word, String docName) {
        TrieNode node = root; // Start at the root node
        // Traverse through the Trie, creating nodes as needed for each character in the word
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        // Add the document name to the list if it's not already present to avoid duplicates
        if (!node.docNames.contains(docName)) {
            node.docNames.add(docName); // Add the document name to the list at the end of the word
        }
    }

    // Method to search for a word in the Trie and return the list of document names containing the word
    public List<String> search(String word) {
        TrieNode node = root; // Start at the root node
        // Traverse through the Trie character by character
        for (char c : word.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                // If the word is not found, return an empty list
                return Collections.emptyList();
            }
        }
        // Return the list of document names containing the word
        return node.docNames;
    }
}

// Main class to build the inverted index and perform searches using the Trie
public class InvertedIndexUsingTrie {
    public static void main(String[] args) {
        Trie trie = new Trie(); // Create a new Trie instance
        // Array of CSV file paths to be indexed
        String[] csvFiles = {
                "C:/Users/helir/Downloads/Expedia.csv",
                "C:/Users/helir/Downloads/Kayak_Data_Asg.csv",
                "C:/Users/helir/Downloads/travelocity.csv",
                "C:/Users/helir/Downloads/AirCanada_Data.csv",
                "C:/Users/helir/Downloads/flight_data.csv",
                "C:/Users/helir/Downloads/flightdata.csv"
        };

        // Loop through each CSV file to build the inverted index
        for (String csvFile : csvFiles) {
            // Using try-with-resources to automatically close the BufferedReader
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line; // Variable to hold each line read from the file
                // Read each line from the CSV file until end of file
                while ((line = br.readLine()) != null) {
                    // Split each line into words based on non-word characters (e.g., spaces, punctuation)
                    String[] words = line.split("\\W+");
                    // Loop through each word in the line
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            // Insert each word into the Trie with the corresponding document name
                            trie.insert(word.toLowerCase(), csvFile);
                        }
                    }
                }
            } catch (IOException e) {
                // Handle potential IO exceptions
                e.printStackTrace();
            }
        }

        // Create a Scanner object to read input from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter word to search: ");
        // Read the word to search from the user
        String word = scanner.nextLine().toLowerCase();

        // Search for the word in the Trie and get the list of documents containing the word
        List<String> result = trie.search(word);
        // Check if any documents contain the word
        if (result.isEmpty()) {
            // If no documents contain the word, print a message
            System.out.println("No documents contain the word: " + word);
        } else {
            // Print the list of documents containing the word
            System.out.println("Documents containing the word '" + word + "':");
            for (String docName : result) {
                // Print each document name
                System.out.println(docName);
            }
        }

        // Close the scanner to free up resources
        scanner.close();
    }
}
