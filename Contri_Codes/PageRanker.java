package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PageRanker {

    // Method to read CSV file and return the content of two web pages separated by the date of traveling
    public static List<String> readCSV(String filePath) {
        List<String> pages = new ArrayList<>();
        StringBuilder page1 = new StringBuilder();
        StringBuilder page2 = new StringBuilder();
        boolean isPage2 = false;
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/Umesh Maurya/Downloads/travelocity.csv"))) {
            while ((line = br.readLine()) != null) {
                if (line.contains("For July 1")) {
                    isPage2 = true;
                    continue;
                }
                if (isPage2) {
                    page2.append(line).append("\n");
                } else {
                    page1.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        pages.add(page1.toString());
        pages.add(page2.toString());
        return pages;
    }

    // Boyer-Moore algorithm implementation
    public static class BoyerMoore {
        private final int R; // the radix
        private int[] right; // the bad-character skip array
        private String pat; // the pattern string

        public BoyerMoore(String pat) {
            this.R = 65536; // For Unicode characters
            this.pat = pat;
            right = new int[R];
            Arrays.fill(right, -1);
            for (int j = 0; j < pat.length(); j++)
                right[pat.charAt(j)] = j;
        }

        public int search(String txt) {
            int M = pat.length();
            int N = txt.length();
            int skip;
            int count = 0;
            for (int i = 0; i <= N - M; i += skip) {
                skip = 0;
                for (int j = M - 1; j >= 0; j--) {
                    if (pat.charAt(j) != txt.charAt(i + j)) {
                        skip = Math.max(1, j - right[txt.charAt(i + j)]);
                        break;
                    }
                }
                if (skip == 0) {
                    count++;
                    skip++;
                }
            }
            return count;
        }
    }

    public static void main(String[] args) {
        List<String> pages = readCSV("/mnt/data/travelocity.csv");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the keyword to search:");
        String keyword = scanner.nextLine();
        scanner.close();

        BoyerMoore bm = new BoyerMoore(keyword);
        Map<Integer, Integer> pageCounts = new HashMap<>();

        int pageIndex = 1;
        for (String page : pages) {
            int count = bm.search(page);
            pageCounts.put(pageIndex++, count);
        }

        List<Map.Entry<Integer, Integer>> sortedPages = new ArrayList<>(pageCounts.entrySet());
        sortedPages.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        System.out.println("Ranked list of web pages:");
        for (Map.Entry<Integer, Integer> entry : sortedPages) {
            System.out.println("Keyword '" + keyword + "' appears " + entry.getValue() + " times in Page " + entry.getKey() + ".");
        }

        // Check if multiple pages have the same rank
        if (sortedPages.size() > 1) {
            int firstCount = sortedPages.get(0).getValue();
            boolean allSameRank = true;
            for (Map.Entry<Integer, Integer> entry : sortedPages) {
                if (entry.getValue() != firstCount) {
                    allSameRank = false;
                    break;
                }
            }
            if (allSameRank) {
                System.out.println("Both pages have the same rank for the keyword.");
            } else {
                int highestPage = sortedPages.get(0).getKey();
                System.out.println("Page " + highestPage + " is higher due to higher word count.");
            }
        }
    }
}
