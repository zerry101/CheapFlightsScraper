import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Define node structure for trie //Priyal Thakkar
class TrieNode {
    Map<Character, TrieNode> children; // Store child nodes for each character //Priyal Thakkar
    boolean isEndOfWord; // Indicate end of a word

    // Constructor to initialize node //Priyal Thakkar
    public TrieNode() {
        children = new HashMap<>(); // Initialize children map //Priyal Thakkar
        isEndOfWord = false; // Set end of word to false //Priyal Thakkar
    }
}

// Define trie data structure //Priyal Thakkar
class Trie {
    private TrieNode root; // Root node of trie //Priyal Thakkar

    // Constructor to initialize trie //Priyal Thakkar
    public Trie() {
        root = new TrieNode(); // Create root node //Priyal Thakkar
    }

    // Insert word into trie //Priyal Thakkar
    public void insert(String word) {
        TrieNode current = root; // Start from root //Priyal Thakkar
        for (char ch : word.toCharArray()) { // Traverse each character //Priyal Thakkar
            current = current.children.computeIfAbsent(ch, c -> new TrieNode()); // Add child node if absent //Priyal Thakkar
        }
        current.isEndOfWord = true; // Mark end of word //Priyal Thakkar
    }

    // Search word in trie
    public boolean search(String word) {
        TrieNode current = root; // Start from root //Priyal Thakkar
        for (char ch : word.toCharArray()) { // Traverse each character //Priyal Thakkar
            current = current.children.get(ch); // Move to child node //Priyal Thakkar
            if (current == null) { // If character not found //Priyal Thakkar
                return false; // Word does not exist //Priyal Thakkar
            }
        }
        return current.isEndOfWord; // Return if it is end of word //Priyal Thakkar
    }

    // Retrieve all words in trie //Priyal Thakkar
    public List<String> getAllWords() {
        List<String> words = new ArrayList<>(); // Initialize list for words //Priyal Thakkar
        collectWords(root, "", words); // Collect words from root //Priyal Thakkar
        return words; // Return list of words //Priyal Thakkar
    }

    // Helper method to collect words //Priyal Thakkar
    private void collectWords(TrieNode node, String prefix, List<String> words) {
        if (node.isEndOfWord) { // Check if it is end of word //Priyal Thakkar
            words.add(prefix); // Add word to list //Priyal Thakkar
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) { // Traverse child nodes //Priyal Thakkar
            collectWords(entry.getValue(), prefix + entry.getKey(), words); // Collect words recursively //Priyal Thakkar
        }
    }
}

// Class to calculate edit distance between words //Priyal Thakkar
class EditDistance {
    public static int calculate(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1]; // Initialize DP array //Priyal Thakkar

        for (int i = 0; i <= word1.length(); i++) { // Iterate over length of word1 //Priyal Thakkar
            for (int j = 0; j <= word2.length(); j++) { // Iterate over length of word2 //Priyal Thakkar
                if (i == 0) { // If first word is empty //Priyal Thakkar
                    dp[i][j] = j; // Cost is length of second word //Priyal Thakkar
                } else if (j == 0) { // If second word is empty //Priyal Thakkar
                    dp[i][j] = i; // Cost is length of first word //Priyal Thakkar
                } else { // Calculate cost for current position //Priyal Thakkar
                    dp[i][j] = Math.min(dp[i - 1][j - 1]
                                    + (word1.charAt(i - 1) == word2.charAt(j - 1) ? 0 : 1), // Check characters //Priyal Thakkar
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)); // Check insert/delete operations //Priyal Thakkar
                }
            }
        }
        return dp[word1.length()][word2.length()]; // Return calculated distance //Priyal Thakkar
    }
}

// Spell checker class using trie //Priyal Thakkar
class SpellChecker {
    private Trie trie; // Trie for storing vocabulary //Priyal Thakkar

    // Constructor to initialize spell checker //Priyal Thakkar
    public SpellChecker() {
        trie = new Trie(); // Create new trie //Priyal Thakkar
    }

    // Load vocabulary into trie //Priyal Thakkar
    public void loadVocabulary(List<String> words) {
        for (String word : words) { // Traverse each word //Priyal Thakkar
            trie.insert(word); // Insert word into trie //Priyal Thakkar
        }
    }

    // Check if word exists in vocabulary //Priyal Thakkar
    public boolean checkWord(String word) {
        return trie.search(word); // Search word in trie //Priyal Thakkar
    }

    // Suggest alternatives for misspelled word //Priyal Thakkar
    public List<String> suggestAlternatives(String word, int maxEditDistance) {
        List<String> suggestions = new ArrayList<>(); // Initialize list for suggestions //Priyal Thakkar
        for (String dictWord : trie.getAllWords()) { // Retrieve all words from trie //Priyal Thakkar
            if (EditDistance.calculate(word, dictWord) <= maxEditDistance) { // Check edit distance //Priyal Thakkar
                suggestions.add(dictWord); // Add to suggestions if within distance //Priyal Thakkar
            }
        }
        return suggestions; // Return list of suggestions //Priyal Thakkar
    }

    // Load vocabulary from CSV file //Priyal Thakkar
    public void loadVocabularyFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Open file //Priyal Thakkar
            String line; // Read each line //Priyal Thakkar
            while ((line = br.readLine()) != null) { // Continue till end of file //Priyal Thakkar
                String[] words = line.split(","); // Split line into words //Priyal Thakkar
                for (String word : words) { // Traverse each word //Priyal Thakkar
                    trie.insert(word.trim()); // Insert word into trie //Priyal Thakkar
                }
            }
        } catch (IOException e) { // Handle I/O exceptions //Priyal Thakkar
            e.printStackTrace(); // Print stack trace //Priyal Thakkar
        }
    }
}

// Main class to demonstrate spell checker //Priyal Thakkar
public class SpellCheckerTries {
    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker(); // Initialize spell checker //Priyal Thakkar
        String csvFilePath = "C://Users//priya//IdeaProjects//Priyal_Asg1//Kayak_Data_Asg.csv"; // Define file path //Priyal Thakkar

        spellChecker.loadVocabularyFromCSV(csvFilePath); // Load vocabulary from CSV //Priyal Thakkar

        Scanner scanner = new Scanner(System.in); // Initialize scanner for input //Priyal Thakkar
        System.out.println("Enter a word to check: "); // Prompt user for word //Priyal Thakkar
        String word = scanner.nextLine(); // Read user input //Priyal Thakkar

        if (spellChecker.checkWord(word)) { // Check if word exists //Priyal Thakkar
            System.out.println("Word exists in vocabulary."); // Inform user //Priyal Thakkar
        } else {
            System.out.println("Word does not exist in vocabulary."); // Inform user //Priyal Thakkar
            List<String> suggestions = spellChecker.suggestAlternatives(word, 2); // Suggest alternatives //Priyal Thakkar
            System.out.println("Did you mean: " + suggestions); // Print suggestions //Priyal Thakkar
        }
    }
}
